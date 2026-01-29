package com.honeyai.repository;

import com.honeyai.model.HistoriqueEtiquettes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class HistoriqueEtiquettesRepositoryTest {

    @Autowired
    private HistoriqueEtiquettesRepository repository;

    @Test
    void findTop20ByOrderByDateGenerationDesc_shouldReturnEmptyList_whenNoHistory() {
        List<HistoriqueEtiquettes> result = repository.findTop20ByOrderByDateGenerationDesc();

        assertThat(result).isEmpty();
    }

    @Test
    void findTop20ByOrderByDateGenerationDesc_shouldReturnOrderedByDateDesc() {
        // Given
        HistoriqueEtiquettes older = createHistorique(LocalDateTime.now().minusDays(2));
        HistoriqueEtiquettes newer = createHistorique(LocalDateTime.now().minusDays(1));
        HistoriqueEtiquettes newest = createHistorique(LocalDateTime.now());

        repository.save(older);
        repository.save(newer);
        repository.save(newest);

        // When
        List<HistoriqueEtiquettes> result = repository.findTop20ByOrderByDateGenerationDesc();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getDateGeneration()).isAfter(result.get(1).getDateGeneration());
        assertThat(result.get(1).getDateGeneration()).isAfter(result.get(2).getDateGeneration());
    }

    @Test
    void findTop20ByOrderByDateGenerationDesc_shouldLimitTo20() {
        // Given - create 25 records
        for (int i = 0; i < 25; i++) {
            HistoriqueEtiquettes h = createHistorique(LocalDateTime.now().minusMinutes(25 - i));
            repository.save(h);
        }

        // When
        List<HistoriqueEtiquettes> result = repository.findTop20ByOrderByDateGenerationDesc();

        // Then
        assertThat(result).hasSize(20);
    }

    @Test
    void save_shouldPersistAllFields() {
        // Given
        HistoriqueEtiquettes h = HistoriqueEtiquettes.builder()
                .typeMiel("TOUTES_FLEURS")
                .formatPot("POT_500G")
                .dateRecolte(LocalDate.of(2024, 8, 15))
                .dluo(LocalDate.of(2026, 8, 15))
                .quantite(10)
                .dateGeneration(LocalDateTime.now())
                .prixUnitaire(new BigDecimal("8.50"))
                .build();

        // When
        HistoriqueEtiquettes saved = repository.save(h);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTypeMiel()).isEqualTo("TOUTES_FLEURS");
        assertThat(saved.getFormatPot()).isEqualTo("POT_500G");
        assertThat(saved.getDateRecolte()).isEqualTo(LocalDate.of(2024, 8, 15));
        assertThat(saved.getDluo()).isEqualTo(LocalDate.of(2026, 8, 15));
        assertThat(saved.getQuantite()).isEqualTo(10);
        assertThat(saved.getPrixUnitaire()).isEqualByComparingTo("8.50");
    }

    @Test
    void save_shouldAllowNullPrice() {
        // Given
        HistoriqueEtiquettes h = createHistorique(LocalDateTime.now());
        h.setPrixUnitaire(null);

        // When
        HistoriqueEtiquettes saved = repository.save(h);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPrixUnitaire()).isNull();
    }

    private HistoriqueEtiquettes createHistorique(LocalDateTime dateGeneration) {
        return HistoriqueEtiquettes.builder()
                .typeMiel("TOUTES_FLEURS")
                .formatPot("POT_500G")
                .dateRecolte(LocalDate.of(2024, 8, 15))
                .dluo(LocalDate.of(2026, 8, 15))
                .quantite(10)
                .dateGeneration(dateGeneration)
                .prixUnitaire(new BigDecimal("8.50"))
                .build();
    }
}
