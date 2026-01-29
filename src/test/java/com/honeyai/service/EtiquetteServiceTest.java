package com.honeyai.service;

import com.honeyai.config.EtiquetteConfig;
import com.honeyai.dto.EtiquetteData;
import com.honeyai.dto.EtiquetteRequest;
import com.honeyai.enums.FormatPot;
import com.honeyai.enums.HoneyType;
import com.honeyai.model.HistoriqueEtiquettes;
import com.honeyai.repository.HistoriqueEtiquettesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtiquetteServiceTest {

    @Mock
    private EtiquetteConfig etiquetteConfig;

    @Mock
    private HistoriqueEtiquettesRepository historiqueEtiquettesRepository;

    @InjectMocks
    private EtiquetteService etiquetteService;

    @BeforeEach
    void setUp() {
        lenient().when(etiquetteConfig.getDluoDureeJours()).thenReturn(730);
    }

    // ==================== calculateDluo Tests ====================

    @Test
    void calculateDluo_shouldReturn2YearsLater_whenDefaultConfig() {
        LocalDate dateRecolte = LocalDate.of(2024, 6, 15);
        LocalDate dluo = etiquetteService.calculateDluo(dateRecolte, 730);
        assertThat(dluo).isEqualTo(LocalDate.of(2026, 6, 15));
    }

    @Test
    void calculateDluo_shouldUseConfigDefault_whenDurationIsNull() {
        LocalDate dateRecolte = LocalDate.of(2024, 6, 15);
        when(etiquetteConfig.getDluoDureeJours()).thenReturn(730);
        LocalDate dluo = etiquetteService.calculateDluo(dateRecolte, null);
        assertThat(dluo).isEqualTo(LocalDate.of(2026, 6, 15));
    }

    @Test
    void calculateDluo_shouldUseDefaultOverload_fromConfig() {
        LocalDate dateRecolte = LocalDate.of(2024, 8, 20);
        when(etiquetteConfig.getDluoDureeJours()).thenReturn(730);
        LocalDate dluo = etiquetteService.calculateDluo(dateRecolte);
        assertThat(dluo).isEqualTo(LocalDate.of(2026, 8, 20));
    }

    @Test
    void calculateDluo_shouldThrowException_whenDateRecolteIsNull() {
        assertThatThrownBy(() -> etiquetteService.calculateDluo(null, 730))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date de récolte");
    }

    @Test
    void calculateDluo_shouldHandleYearBoundary() {
        LocalDate dateRecolte = LocalDate.of(2024, 12, 15);
        LocalDate dluo = etiquetteService.calculateDluo(dateRecolte, 730);
        assertThat(dluo).isEqualTo(LocalDate.of(2026, 12, 15));
    }

    @Test
    void calculateDluo_shouldHandleLeapYear() {
        LocalDate dateRecolte = LocalDate.of(2024, 2, 29);
        LocalDate dluo = etiquetteService.calculateDluo(dateRecolte, 730);
        assertThat(dluo).isEqualTo(LocalDate.of(2026, 2, 28));
    }

    // ==================== formatDluo Tests ====================

    @Test
    void formatDluo_shouldReturnMMYYYY_format() {
        assertThat(etiquetteService.formatDluo(LocalDate.of(2026, 6, 14))).isEqualTo("06/2026");
    }

    @Test
    void formatDluo_shouldPadSingleDigitMonth() {
        assertThat(etiquetteService.formatDluo(LocalDate.of(2026, 1, 15))).isEqualTo("01/2026");
    }

    @Test
    void formatDluo_shouldReturnEmpty_whenDluoIsNull() {
        assertThat(etiquetteService.formatDluo(null)).isEmpty();
    }

    @Test
    void formatDluo_shouldHandleDecember() {
        assertThat(etiquetteService.formatDluo(LocalDate.of(2026, 12, 31))).isEqualTo("12/2026");
    }

    // ==================== getTypeAbbreviation Tests ====================

    @Test
    void getTypeAbbreviation_shouldReturnTF_forToutesFleurs() {
        assertThat(etiquetteService.getTypeAbbreviation(HoneyType.TOUTES_FLEURS)).isEqualTo("TF");
    }

    @Test
    void getTypeAbbreviation_shouldReturnFOR_forForet() {
        assertThat(etiquetteService.getTypeAbbreviation(HoneyType.FORET)).isEqualTo("FOR");
    }

    @Test
    void getTypeAbbreviation_shouldReturnCHA_forChataignier() {
        assertThat(etiquetteService.getTypeAbbreviation(HoneyType.CHATAIGNIER)).isEqualTo("CHA");
    }

    // ==================== buildEtiquetteData Tests ====================

    @Test
    void buildEtiquetteData_shouldBuildCompleteData() {
        EtiquetteRequest request = EtiquetteRequest.builder()
                .typeMiel(HoneyType.TOUTES_FLEURS)
                .formatPot(FormatPot.POT_500G)
                .dateRecolte(LocalDate.of(2024, 8, 15))
                .build();

        when(etiquetteConfig.getDluoDureeJours()).thenReturn(730);
        when(etiquetteConfig.getNomApiculteur()).thenReturn("Test Apiculteur");
        when(etiquetteConfig.getAdresse()).thenReturn("123 Test Street");
        when(etiquetteConfig.getSiret()).thenReturn("12345678901234");
        when(etiquetteConfig.getTelephone()).thenReturn("0612345678");

        EtiquetteData data = etiquetteService.buildEtiquetteData(request);

        assertThat(data).isNotNull();
        assertThat(data.getTypeMiel()).isEqualTo("Toutes Fleurs");
        assertThat(data.getFormatPot()).isEqualTo("500g");
        assertThat(data.getPoids()).isEqualTo("Poids net: 500g");
        assertThat(data.getDateRecolte()).isEqualTo("Récolte: 08/2024");
        assertThat(data.getDluo()).isEqualTo(LocalDate.of(2026, 8, 15));
        assertThat(data.getNomApiculteur()).isEqualTo("Test Apiculteur");
        assertThat(data.getAdresse()).isEqualTo("123 Test Street");
        assertThat(data.getSiret()).isEqualTo("12345678901234");
        assertThat(data.getTelephone()).isEqualTo("0612345678");
    }

    @Test
    void buildEtiquetteData_shouldThrowException_forNullRequest() {
        assertThatThrownBy(() -> etiquetteService.buildEtiquetteData(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("requête d'étiquette");
    }

    @Test
    void buildEtiquetteData_shouldBuildForForetType() {
        EtiquetteRequest request = EtiquetteRequest.builder()
                .typeMiel(HoneyType.FORET)
                .formatPot(FormatPot.POT_1KG)
                .dateRecolte(LocalDate.of(2024, 9, 1))
                .build();

        when(etiquetteConfig.getDluoDureeJours()).thenReturn(730);
        when(etiquetteConfig.getNomApiculteur()).thenReturn("Apiculteur");
        when(etiquetteConfig.getAdresse()).thenReturn("Adresse");
        when(etiquetteConfig.getSiret()).thenReturn("SIRET");
        when(etiquetteConfig.getTelephone()).thenReturn("Tel");

        EtiquetteData data = etiquetteService.buildEtiquetteData(request);

        assertThat(data.getTypeMiel()).isEqualTo("Forêt");
        assertThat(data.getFormatPot()).isEqualTo("1kg");
        assertThat(data.getPoids()).isEqualTo("Poids net: 1kg");
    }

    // ==================== saveHistorique Tests ====================

    @Test
    void saveHistorique_shouldSaveAllFields() {
        EtiquetteRequest request = EtiquetteRequest.builder()
                .typeMiel(HoneyType.TOUTES_FLEURS)
                .formatPot(FormatPot.POT_500G)
                .dateRecolte(LocalDate.of(2024, 8, 15))
                .build();

        EtiquetteData data = EtiquetteData.builder()
                .dluo(LocalDate.of(2026, 8, 15))
                .build();

        BigDecimal price = new BigDecimal("8.50");

        when(historiqueEtiquettesRepository.save(any(HistoriqueEtiquettes.class)))
                .thenAnswer(inv -> {
                    HistoriqueEtiquettes h = inv.getArgument(0);
                    h.setId(1L);
                    return h;
                });

        HistoriqueEtiquettes saved = etiquetteService.saveHistorique(request, data, price, 21);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(1L);

        ArgumentCaptor<HistoriqueEtiquettes> captor = ArgumentCaptor.forClass(HistoriqueEtiquettes.class);
        verify(historiqueEtiquettesRepository).save(captor.capture());

        HistoriqueEtiquettes captured = captor.getValue();
        assertThat(captured.getTypeMiel()).isEqualTo("TOUTES_FLEURS");
        assertThat(captured.getFormatPot()).isEqualTo("POT_500G");
        assertThat(captured.getDateRecolte()).isEqualTo(LocalDate.of(2024, 8, 15));
        assertThat(captured.getDluo()).isEqualTo(LocalDate.of(2026, 8, 15));
        assertThat(captured.getQuantite()).isEqualTo(21);
        assertThat(captured.getDateGeneration()).isNotNull();
        assertThat(captured.getPrixUnitaire()).isEqualByComparingTo("8.50");
    }

    @Test
    void saveHistorique_shouldAllowNullPrice() {
        EtiquetteRequest request = EtiquetteRequest.builder()
                .typeMiel(HoneyType.FORET)
                .formatPot(FormatPot.POT_1KG)
                .dateRecolte(LocalDate.of(2024, 9, 1))
                .build();

        EtiquetteData data = EtiquetteData.builder()
                .dluo(LocalDate.of(2026, 9, 1))
                .build();

        when(historiqueEtiquettesRepository.save(any(HistoriqueEtiquettes.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        etiquetteService.saveHistorique(request, data, null, 21);

        ArgumentCaptor<HistoriqueEtiquettes> captor = ArgumentCaptor.forClass(HistoriqueEtiquettes.class);
        verify(historiqueEtiquettesRepository).save(captor.capture());
        assertThat(captor.getValue().getPrixUnitaire()).isNull();
    }

    // ==================== getRecentHistorique Tests ====================

    @Test
    void getRecentHistorique_shouldReturnEmptyList_whenNoHistory() {
        when(historiqueEtiquettesRepository.findTop20ByOrderByDateGenerationDesc())
                .thenReturn(List.of());

        List<HistoriqueEtiquettes> result = etiquetteService.getRecentHistorique();

        assertThat(result).isEmpty();
        verify(historiqueEtiquettesRepository).findTop20ByOrderByDateGenerationDesc();
    }

    @Test
    void getRecentHistorique_shouldReturnHistoryList() {
        HistoriqueEtiquettes h1 = HistoriqueEtiquettes.builder()
                .id(1L)
                .typeMiel("TOUTES_FLEURS")
                .dateGeneration(LocalDateTime.now())
                .build();
        HistoriqueEtiquettes h2 = HistoriqueEtiquettes.builder()
                .id(2L)
                .typeMiel("FORET")
                .dateGeneration(LocalDateTime.now().minusHours(1))
                .build();

        when(historiqueEtiquettesRepository.findTop20ByOrderByDateGenerationDesc())
                .thenReturn(List.of(h1, h2));

        List<HistoriqueEtiquettes> result = etiquetteService.getRecentHistorique();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTypeMiel()).isEqualTo("TOUTES_FLEURS");
        assertThat(result.get(1).getTypeMiel()).isEqualTo("FORET");
    }
}
