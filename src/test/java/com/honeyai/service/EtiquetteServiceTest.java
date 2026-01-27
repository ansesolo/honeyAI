package com.honeyai.service;

import com.honeyai.config.EtiquetteConfig;
import com.honeyai.dto.EtiquetteData;
import com.honeyai.dto.EtiquetteRequest;
import com.honeyai.enums.FormatPot;
import com.honeyai.enums.HoneyType;
import com.honeyai.model.HistoriqueEtiquettes;
import com.honeyai.model.LotsEtiquettes;
import com.honeyai.repository.HistoriqueEtiquettesRepository;
import com.honeyai.repository.LotsEtiquettesRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtiquetteServiceTest {

    @Mock
    private EtiquetteConfig etiquetteConfig;

    @Mock
    private LotsEtiquettesRepository lotsEtiquettesRepository;

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
        // Given
        LocalDate dateRecolte = LocalDate.of(2024, 6, 15);

        // When
        LocalDate dluo = etiquetteService.calculateDluo(dateRecolte, 730);

        // Then - 730 days from 2024-06-15 is 2026-06-15
        assertThat(dluo).isEqualTo(LocalDate.of(2026, 6, 15));
    }

    @Test
    void calculateDluo_shouldUseConfigDefault_whenDurationIsNull() {
        // Given
        LocalDate dateRecolte = LocalDate.of(2024, 6, 15);
        when(etiquetteConfig.getDluoDureeJours()).thenReturn(730);

        // When
        LocalDate dluo = etiquetteService.calculateDluo(dateRecolte, null);

        // Then - 730 days from 2024-06-15 is 2026-06-15
        assertThat(dluo).isEqualTo(LocalDate.of(2026, 6, 15));
    }

    @Test
    void calculateDluo_shouldUseDefaultOverload_fromConfig() {
        // Given
        LocalDate dateRecolte = LocalDate.of(2024, 8, 20);
        when(etiquetteConfig.getDluoDureeJours()).thenReturn(730);

        // When
        LocalDate dluo = etiquetteService.calculateDluo(dateRecolte);

        // Then - 730 days from 2024-08-20 is 2026-08-20
        assertThat(dluo).isEqualTo(LocalDate.of(2026, 8, 20));
    }

    @Test
    void calculateDluo_shouldThrowException_whenDateRecolteIsNull() {
        // When/Then
        assertThatThrownBy(() -> etiquetteService.calculateDluo(null, 730))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date de récolte");
    }

    @Test
    void calculateDluo_shouldHandleYearBoundary() {
        // Given - harvest date late in the year
        LocalDate dateRecolte = LocalDate.of(2024, 12, 15);

        // When
        LocalDate dluo = etiquetteService.calculateDluo(dateRecolte, 730);

        // Then - 730 days from 2024-12-15 is 2026-12-15
        assertThat(dluo).isEqualTo(LocalDate.of(2026, 12, 15));
    }

    @Test
    void calculateDluo_shouldHandleLeapYear() {
        // Given - harvest on Feb 29 of a leap year
        LocalDate dateRecolte = LocalDate.of(2024, 2, 29);

        // When
        LocalDate dluo = etiquetteService.calculateDluo(dateRecolte, 730);

        // Then - 730 days from Feb 29, 2024
        assertThat(dluo).isEqualTo(LocalDate.of(2026, 2, 28));
    }

    // ==================== formatDluo Tests ====================

    @Test
    void formatDluo_shouldReturnMMYYYY_format() {
        // Given
        LocalDate dluo = LocalDate.of(2026, 6, 14);

        // When
        String formatted = etiquetteService.formatDluo(dluo);

        // Then
        assertThat(formatted).isEqualTo("06/2026");
    }

    @Test
    void formatDluo_shouldPadSingleDigitMonth() {
        // Given
        LocalDate dluo = LocalDate.of(2026, 1, 15);

        // When
        String formatted = etiquetteService.formatDluo(dluo);

        // Then
        assertThat(formatted).isEqualTo("01/2026");
    }

    @Test
    void formatDluo_shouldReturnEmpty_whenDluoIsNull() {
        // When
        String formatted = etiquetteService.formatDluo(null);

        // Then
        assertThat(formatted).isEmpty();
    }

    @Test
    void formatDluo_shouldHandleDecember() {
        // Given
        LocalDate dluo = LocalDate.of(2026, 12, 31);

        // When
        String formatted = etiquetteService.formatDluo(dluo);

        // Then
        assertThat(formatted).isEqualTo("12/2026");
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

    // ==================== generateNumeroLot Tests ====================

    @Test
    void generateNumeroLot_shouldGenerateFirstLotNumber_forNewYearType() {
        // Given
        LocalDate dateRecolte = LocalDate.of(2024, 6, 15);
        when(lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "TF"))
                .thenReturn(Optional.empty());
        when(lotsEtiquettesRepository.save(any(LotsEtiquettes.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        String lotNumber = etiquetteService.generateNumeroLot(HoneyType.TOUTES_FLEURS, dateRecolte);

        // Then
        assertThat(lotNumber).isEqualTo("2024-TF-001");

        ArgumentCaptor<LotsEtiquettes> captor = ArgumentCaptor.forClass(LotsEtiquettes.class);
        verify(lotsEtiquettesRepository).save(captor.capture());
        assertThat(captor.getValue().getDernierNumero()).isEqualTo(1);
        assertThat(captor.getValue().getAnnee()).isEqualTo(2024);
        assertThat(captor.getValue().getTypeMiel()).isEqualTo("TF");
    }

    @Test
    void generateNumeroLot_shouldIncrementSequence_forExistingYearType() {
        // Given
        LocalDate dateRecolte = LocalDate.of(2024, 6, 15);
        LotsEtiquettes existingLot = LotsEtiquettes.builder()
                .id(1L)
                .annee(2024)
                .typeMiel("TF")
                .dernierNumero(5)
                .build();
        when(lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "TF"))
                .thenReturn(Optional.of(existingLot));
        when(lotsEtiquettesRepository.save(any(LotsEtiquettes.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        String lotNumber = etiquetteService.generateNumeroLot(HoneyType.TOUTES_FLEURS, dateRecolte);

        // Then
        assertThat(lotNumber).isEqualTo("2024-TF-006");
    }

    @Test
    void generateNumeroLot_shouldReturn002_onSecondCall() {
        // Given
        LocalDate dateRecolte = LocalDate.of(2024, 6, 15);
        LotsEtiquettes existingLot = LotsEtiquettes.builder()
                .id(1L)
                .annee(2024)
                .typeMiel("TF")
                .dernierNumero(1)
                .build();
        when(lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "TF"))
                .thenReturn(Optional.of(existingLot));
        when(lotsEtiquettesRepository.save(any(LotsEtiquettes.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        String lotNumber = etiquetteService.generateNumeroLot(HoneyType.TOUTES_FLEURS, dateRecolte);

        // Then
        assertThat(lotNumber).isEqualTo("2024-TF-002");
    }

    @Test
    void generateNumeroLot_shouldGenerateSeparateSequence_forDifferentTypes() {
        // Given - First call for FORET
        LocalDate dateRecolte = LocalDate.of(2024, 6, 15);
        when(lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "FOR"))
                .thenReturn(Optional.empty());
        when(lotsEtiquettesRepository.save(any(LotsEtiquettes.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        String lotNumber = etiquetteService.generateNumeroLot(HoneyType.FORET, dateRecolte);

        // Then
        assertThat(lotNumber).isEqualTo("2024-FOR-001");
    }

    @Test
    void generateNumeroLot_shouldStartNewSequence_forNewYear() {
        // Given - 2025 lot (new year after 2024-TF-999)
        LocalDate dateRecolte = LocalDate.of(2025, 1, 10);
        when(lotsEtiquettesRepository.findByAnneeAndTypeMiel(2025, "TF"))
                .thenReturn(Optional.empty());
        when(lotsEtiquettesRepository.save(any(LotsEtiquettes.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        String lotNumber = etiquetteService.generateNumeroLot(HoneyType.TOUTES_FLEURS, dateRecolte);

        // Then
        assertThat(lotNumber).isEqualTo("2025-TF-001");
    }

    @Test
    void generateNumeroLot_shouldThrowException_whenHoneyTypeIsNull() {
        // Given
        LocalDate dateRecolte = LocalDate.of(2024, 6, 15);

        // When/Then
        assertThatThrownBy(() -> etiquetteService.generateNumeroLot(null, dateRecolte))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type de miel");
    }

    @Test
    void generateNumeroLot_shouldThrowException_whenDateRecolteIsNull() {
        // When/Then
        assertThatThrownBy(() -> etiquetteService.generateNumeroLot(HoneyType.TOUTES_FLEURS, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date de récolte");
    }

    @Test
    void generateNumeroLot_shouldGenerateForChataignier() {
        // Given
        LocalDate dateRecolte = LocalDate.of(2024, 9, 1);
        when(lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "CHA"))
                .thenReturn(Optional.empty());
        when(lotsEtiquettesRepository.save(any(LotsEtiquettes.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        String lotNumber = etiquetteService.generateNumeroLot(HoneyType.CHATAIGNIER, dateRecolte);

        // Then
        assertThat(lotNumber).isEqualTo("2024-CHA-001");
    }

    @Test
    void generateNumeroLot_shouldHandleHighSequenceNumbers() {
        // Given - Sequence at 999
        LocalDate dateRecolte = LocalDate.of(2024, 6, 15);
        LotsEtiquettes existingLot = LotsEtiquettes.builder()
                .id(1L)
                .annee(2024)
                .typeMiel("TF")
                .dernierNumero(999)
                .build();
        when(lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "TF"))
                .thenReturn(Optional.of(existingLot));
        when(lotsEtiquettesRepository.save(any(LotsEtiquettes.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        String lotNumber = etiquetteService.generateNumeroLot(HoneyType.TOUTES_FLEURS, dateRecolte);

        // Then - Should go to 1000 (4 digits)
        assertThat(lotNumber).isEqualTo("2024-TF-1000");
    }

    // ==================== getCurrentSequenceNumber Tests ====================

    @Test
    void getCurrentSequenceNumber_shouldReturnZero_whenNoLotsExist() {
        // Given
        when(lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "TF"))
                .thenReturn(Optional.empty());

        // When
        int sequence = etiquetteService.getCurrentSequenceNumber(HoneyType.TOUTES_FLEURS, 2024);

        // Then
        assertThat(sequence).isZero();
    }

    @Test
    void getCurrentSequenceNumber_shouldReturnCurrentNumber_whenLotsExist() {
        // Given
        LotsEtiquettes existingLot = LotsEtiquettes.builder()
                .annee(2024)
                .typeMiel("TF")
                .dernierNumero(15)
                .build();
        when(lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "TF"))
                .thenReturn(Optional.of(existingLot));

        // When
        int sequence = etiquetteService.getCurrentSequenceNumber(HoneyType.TOUTES_FLEURS, 2024);

        // Then
        assertThat(sequence).isEqualTo(15);
    }

    // ==================== buildEtiquetteData Tests ====================

    @Test
    void buildEtiquetteData_shouldBuildCompleteData() {
        // Given
        EtiquetteRequest request = EtiquetteRequest.builder()
                .typeMiel(HoneyType.TOUTES_FLEURS)
                .formatPot(FormatPot.POT_500G)
                .dateRecolte(LocalDate.of(2024, 8, 15))
                .quantite(10)
                .build();

        when(etiquetteConfig.getDluoDureeJours()).thenReturn(730);
        when(etiquetteConfig.getNomApiculteur()).thenReturn("Test Apiculteur");
        when(etiquetteConfig.getAdresse()).thenReturn("123 Test Street");
        when(etiquetteConfig.getSiret()).thenReturn("12345678901234");
        when(etiquetteConfig.getTelephone()).thenReturn("0612345678");
        when(lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "TF"))
                .thenReturn(Optional.empty());
        when(lotsEtiquettesRepository.save(any(LotsEtiquettes.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        EtiquetteData data = etiquetteService.buildEtiquetteData(request);

        // Then
        assertThat(data).isNotNull();
        assertThat(data.getTypeMiel()).isEqualTo("Toutes Fleurs");
        assertThat(data.getFormatPot()).isEqualTo("500g");
        assertThat(data.getPoids()).isEqualTo("Poids net: 500g");
        assertThat(data.getDateRecolte()).isEqualTo("Récolte: 08/2024");
        assertThat(data.getDluo()).isEqualTo(LocalDate.of(2026, 8, 15));
        assertThat(data.getNumeroLot()).isEqualTo("2024-TF-001");
        assertThat(data.getNomApiculteur()).isEqualTo("Test Apiculteur");
        assertThat(data.getAdresse()).isEqualTo("123 Test Street");
        assertThat(data.getSiret()).isEqualTo("12345678901234");
        assertThat(data.getTelephone()).isEqualTo("0612345678");
    }

    @Test
    void buildEtiquetteData_shouldThrowException_forNullRequest() {
        // When/Then
        assertThatThrownBy(() -> etiquetteService.buildEtiquetteData(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("requête d'étiquette");
    }

    @Test
    void buildEtiquetteData_shouldBuildForForetType() {
        // Given
        EtiquetteRequest request = EtiquetteRequest.builder()
                .typeMiel(HoneyType.FORET)
                .formatPot(FormatPot.POT_1KG)
                .dateRecolte(LocalDate.of(2024, 9, 1))
                .quantite(5)
                .build();

        when(etiquetteConfig.getDluoDureeJours()).thenReturn(730);
        when(etiquetteConfig.getNomApiculteur()).thenReturn("Apiculteur");
        when(etiquetteConfig.getAdresse()).thenReturn("Adresse");
        when(etiquetteConfig.getSiret()).thenReturn("SIRET");
        when(etiquetteConfig.getTelephone()).thenReturn("Tel");
        when(lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "FOR"))
                .thenReturn(Optional.empty());
        when(lotsEtiquettesRepository.save(any(LotsEtiquettes.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        EtiquetteData data = etiquetteService.buildEtiquetteData(request);

        // Then
        assertThat(data.getTypeMiel()).isEqualTo("Forêt");
        assertThat(data.getFormatPot()).isEqualTo("1kg");
        assertThat(data.getPoids()).isEqualTo("Poids net: 1kg");
        assertThat(data.getNumeroLot()).isEqualTo("2024-FOR-001");
    }

    // ==================== saveHistorique Tests ====================

    @Test
    void saveHistorique_shouldSaveAllFields() {
        // Given
        EtiquetteRequest request = EtiquetteRequest.builder()
                .typeMiel(HoneyType.TOUTES_FLEURS)
                .formatPot(FormatPot.POT_500G)
                .dateRecolte(LocalDate.of(2024, 8, 15))
                .quantite(10)
                .build();

        EtiquetteData data = EtiquetteData.builder()
                .dluo(LocalDate.of(2026, 8, 15))
                .numeroLot("2024-TF-001")
                .build();

        BigDecimal price = new BigDecimal("8.50");

        when(historiqueEtiquettesRepository.save(any(HistoriqueEtiquettes.class)))
                .thenAnswer(inv -> {
                    HistoriqueEtiquettes h = inv.getArgument(0);
                    h.setId(1L);
                    return h;
                });

        // When
        HistoriqueEtiquettes saved = etiquetteService.saveHistorique(request, data, price);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(1L);

        ArgumentCaptor<HistoriqueEtiquettes> captor = ArgumentCaptor.forClass(HistoriqueEtiquettes.class);
        verify(historiqueEtiquettesRepository).save(captor.capture());

        HistoriqueEtiquettes captured = captor.getValue();
        assertThat(captured.getTypeMiel()).isEqualTo("TOUTES_FLEURS");
        assertThat(captured.getFormatPot()).isEqualTo("POT_500G");
        assertThat(captured.getDateRecolte()).isEqualTo(LocalDate.of(2024, 8, 15));
        assertThat(captured.getDluo()).isEqualTo(LocalDate.of(2026, 8, 15));
        assertThat(captured.getNumeroLot()).isEqualTo("2024-TF-001");
        assertThat(captured.getQuantite()).isEqualTo(10);
        assertThat(captured.getDateGeneration()).isNotNull();
        assertThat(captured.getPrixUnitaire()).isEqualByComparingTo("8.50");
    }

    @Test
    void saveHistorique_shouldAllowNullPrice() {
        // Given
        EtiquetteRequest request = EtiquetteRequest.builder()
                .typeMiel(HoneyType.FORET)
                .formatPot(FormatPot.POT_1KG)
                .dateRecolte(LocalDate.of(2024, 9, 1))
                .quantite(5)
                .build();

        EtiquetteData data = EtiquetteData.builder()
                .dluo(LocalDate.of(2026, 9, 1))
                .numeroLot("2024-FOR-001")
                .build();

        when(historiqueEtiquettesRepository.save(any(HistoriqueEtiquettes.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        HistoriqueEtiquettes saved = etiquetteService.saveHistorique(request, data, null);

        // Then
        ArgumentCaptor<HistoriqueEtiquettes> captor = ArgumentCaptor.forClass(HistoriqueEtiquettes.class);
        verify(historiqueEtiquettesRepository).save(captor.capture());
        assertThat(captor.getValue().getPrixUnitaire()).isNull();
    }

    // ==================== getRecentHistorique Tests ====================

    @Test
    void getRecentHistorique_shouldReturnEmptyList_whenNoHistory() {
        // Given
        when(historiqueEtiquettesRepository.findTop20ByOrderByDateGenerationDesc())
                .thenReturn(List.of());

        // When
        List<HistoriqueEtiquettes> result = etiquetteService.getRecentHistorique();

        // Then
        assertThat(result).isEmpty();
        verify(historiqueEtiquettesRepository).findTop20ByOrderByDateGenerationDesc();
    }

    @Test
    void getRecentHistorique_shouldReturnHistoryList() {
        // Given
        HistoriqueEtiquettes h1 = HistoriqueEtiquettes.builder()
                .id(1L)
                .typeMiel("TOUTES_FLEURS")
                .numeroLot("2024-TF-001")
                .dateGeneration(LocalDateTime.now())
                .build();
        HistoriqueEtiquettes h2 = HistoriqueEtiquettes.builder()
                .id(2L)
                .typeMiel("FORET")
                .numeroLot("2024-FOR-001")
                .dateGeneration(LocalDateTime.now().minusHours(1))
                .build();

        when(historiqueEtiquettesRepository.findTop20ByOrderByDateGenerationDesc())
                .thenReturn(List.of(h1, h2));

        // When
        List<HistoriqueEtiquettes> result = etiquetteService.getRecentHistorique();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNumeroLot()).isEqualTo("2024-TF-001");
        assertThat(result.get(1).getNumeroLot()).isEqualTo("2024-FOR-001");
    }
}
