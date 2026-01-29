package com.honeyai.repository;

import com.honeyai.enums.CategorieAchat;
import com.honeyai.model.Achat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AchatRepositoryTest {

    @Autowired
    private AchatRepository achatRepository;

    @BeforeEach
    void setUp() {
        achatRepository.deleteAll();

        achatRepository.save(Achat.builder()
                .dateAchat(LocalDate.of(2025, 3, 10))
                .designation("Cire gaufrée 5kg")
                .montant(new BigDecimal("45.00"))
                .categorie(CategorieAchat.CIRE)
                .build());

        achatRepository.save(Achat.builder()
                .dateAchat(LocalDate.of(2025, 4, 15))
                .designation("Pots verre 500g x100")
                .montant(new BigDecimal("32.50"))
                .categorie(CategorieAchat.POTS)
                .build());

        achatRepository.save(Achat.builder()
                .dateAchat(LocalDate.of(2025, 4, 20))
                .designation("Couvercles dorés x100")
                .montant(new BigDecimal("18.90"))
                .categorie(CategorieAchat.COUVERCLES)
                .build());

        achatRepository.save(Achat.builder()
                .dateAchat(LocalDate.of(2025, 6, 1))
                .designation("Sirop de nourrissement 10L")
                .montant(new BigDecimal("25.00"))
                .categorie(CategorieAchat.NOURRISSEMENT)
                .build());

        achatRepository.save(Achat.builder()
                .dateAchat(LocalDate.of(2025, 7, 5))
                .designation("Gants apiculteur")
                .montant(new BigDecimal("15.99"))
                .categorie(CategorieAchat.AUTRE)
                .notes("Taille L")
                .build());
    }

    @Test
    void save_shouldPersistAchatAndGenerateId() {
        // Given
        Achat achat = Achat.builder()
                .dateAchat(LocalDate.of(2025, 8, 1))
                .designation("Enfumoir inox")
                .montant(new BigDecimal("29.90"))
                .categorie(CategorieAchat.AUTRE)
                .notes("Remplacement ancien")
                .build();

        // When
        Achat saved = achatRepository.save(achat);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDesignation()).isEqualTo("Enfumoir inox");
        assertThat(saved.getMontant()).isEqualByComparingTo("29.90");
        assertThat(saved.getCategorie()).isEqualTo(CategorieAchat.AUTRE);
        assertThat(saved.getNotes()).isEqualTo("Remplacement ancien");
    }

    @Test
    void findById_shouldReturnAchat_whenExists() {
        // Given
        Achat saved = achatRepository.save(Achat.builder()
                .dateAchat(LocalDate.of(2025, 9, 1))
                .designation("Test achat")
                .montant(new BigDecimal("10.00"))
                .categorie(CategorieAchat.CIRE)
                .build());

        // When
        Optional<Achat> result = achatRepository.findById(saved.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getDesignation()).isEqualTo("Test achat");
    }

    @Test
    void findByDateAchatBetween_shouldReturnAchatsInRange() {
        // When
        List<Achat> result = achatRepository.findByDateAchatBetween(
                LocalDate.of(2025, 4, 1),
                LocalDate.of(2025, 4, 30));

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Achat::getCategorie)
                .containsExactlyInAnyOrder(CategorieAchat.POTS, CategorieAchat.COUVERCLES);
    }

    @Test
    void findByCategorie_shouldReturnMatchingAchats() {
        // When
        List<Achat> result = achatRepository.findByCategorie(CategorieAchat.CIRE);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDesignation()).isEqualTo("Cire gaufrée 5kg");
    }

    @Test
    void findByCategorie_shouldReturnEmptyList_whenNoMatch() {
        // Given - no NOURRISSEMENT saved in march range, but let's check a fresh category scenario
        achatRepository.deleteAll();

        // When
        List<Achat> result = achatRepository.findByCategorie(CategorieAchat.CIRE);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findAllByOrderByDateAchatDesc_shouldReturnAllSortedDescending() {
        // When
        List<Achat> result = achatRepository.findAllByOrderByDateAchatDesc();

        // Then
        assertThat(result).hasSize(5);
        assertThat(result.get(0).getDateAchat()).isEqualTo(LocalDate.of(2025, 7, 5));
        assertThat(result.get(4).getDateAchat()).isEqualTo(LocalDate.of(2025, 3, 10));
    }

    @Test
    void save_shouldStoreBigDecimalWithTwoDecimalPlaces() {
        // Given
        Achat achat = Achat.builder()
                .dateAchat(LocalDate.of(2025, 1, 1))
                .designation("Test precision")
                .montant(new BigDecimal("123.45"))
                .categorie(CategorieAchat.AUTRE)
                .build();

        // When
        Achat saved = achatRepository.save(achat);
        Achat retrieved = achatRepository.findById(saved.getId()).orElseThrow();

        // Then
        assertThat(retrieved.getMontant()).isEqualByComparingTo("123.45");
    }
}
