package com.honeyai.repository;

import com.honeyai.model.LotsEtiquettes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class LotsEtiquettesRepositoryTest {

    @Autowired
    private LotsEtiquettesRepository lotsEtiquettesRepository;

    @Test
    void findByAnneeAndTypeMiel_shouldReturnLot_whenExists() {
        // Given
        LotsEtiquettes lot = LotsEtiquettes.builder()
                .annee(2024)
                .typeMiel("TF")
                .dernierNumero(5)
                .build();
        lotsEtiquettesRepository.save(lot);

        // When
        Optional<LotsEtiquettes> result = lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "TF");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getDernierNumero()).isEqualTo(5);
    }

    @Test
    void findByAnneeAndTypeMiel_shouldReturnEmpty_whenNotExists() {
        // When
        Optional<LotsEtiquettes> result = lotsEtiquettesRepository.findByAnneeAndTypeMiel(2099, "TF");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByAnneeAndTypeMiel_shouldDistinguishByYear() {
        // Given
        LotsEtiquettes lot2024 = LotsEtiquettes.builder()
                .annee(2024)
                .typeMiel("TF")
                .dernierNumero(10)
                .build();
        LotsEtiquettes lot2025 = LotsEtiquettes.builder()
                .annee(2025)
                .typeMiel("TF")
                .dernierNumero(3)
                .build();
        lotsEtiquettesRepository.save(lot2024);
        lotsEtiquettesRepository.save(lot2025);

        // When
        Optional<LotsEtiquettes> result2024 = lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "TF");
        Optional<LotsEtiquettes> result2025 = lotsEtiquettesRepository.findByAnneeAndTypeMiel(2025, "TF");

        // Then
        assertThat(result2024).isPresent();
        assertThat(result2024.get().getDernierNumero()).isEqualTo(10);
        assertThat(result2025).isPresent();
        assertThat(result2025.get().getDernierNumero()).isEqualTo(3);
    }

    @Test
    void findByAnneeAndTypeMiel_shouldDistinguishByType() {
        // Given
        LotsEtiquettes lotTF = LotsEtiquettes.builder()
                .annee(2024)
                .typeMiel("TF")
                .dernierNumero(5)
                .build();
        LotsEtiquettes lotFOR = LotsEtiquettes.builder()
                .annee(2024)
                .typeMiel("FOR")
                .dernierNumero(2)
                .build();
        lotsEtiquettesRepository.save(lotTF);
        lotsEtiquettesRepository.save(lotFOR);

        // When
        Optional<LotsEtiquettes> resultTF = lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "TF");
        Optional<LotsEtiquettes> resultFOR = lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "FOR");

        // Then
        assertThat(resultTF).isPresent();
        assertThat(resultTF.get().getDernierNumero()).isEqualTo(5);
        assertThat(resultFOR).isPresent();
        assertThat(resultFOR.get().getDernierNumero()).isEqualTo(2);
    }

    @Test
    void save_shouldUpdateExistingLot() {
        // Given
        LotsEtiquettes lot = LotsEtiquettes.builder()
                .annee(2024)
                .typeMiel("TF")
                .dernierNumero(1)
                .build();
        lot = lotsEtiquettesRepository.save(lot);

        // When
        lot.setDernierNumero(2);
        lotsEtiquettesRepository.save(lot);

        // Then
        Optional<LotsEtiquettes> result = lotsEtiquettesRepository.findByAnneeAndTypeMiel(2024, "TF");
        assertThat(result).isPresent();
        assertThat(result.get().getDernierNumero()).isEqualTo(2);
    }
}
