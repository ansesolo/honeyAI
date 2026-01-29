package com.honeyai.service;

import com.honeyai.enums.CategorieAchat;
import com.honeyai.model.Achat;
import com.honeyai.repository.AchatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchatServiceTest {

    @Mock
    private AchatRepository achatRepository;

    @InjectMocks
    private AchatService achatService;

    private Achat achatCire;
    private Achat achatPots;
    private Achat achatPots2;

    @BeforeEach
    void setUp() {
        achatCire = Achat.builder()
                .id(1L)
                .dateAchat(LocalDate.of(2025, 4, 10))
                .designation("Cire gaufrée 5kg")
                .montant(new BigDecimal("45.00"))
                .categorie(CategorieAchat.CIRE)
                .build();

        achatPots = Achat.builder()
                .id(2L)
                .dateAchat(LocalDate.of(2025, 4, 15))
                .designation("Pots verre 500g x100")
                .montant(new BigDecimal("32.50"))
                .categorie(CategorieAchat.POTS)
                .build();

        achatPots2 = Achat.builder()
                .id(3L)
                .dateAchat(LocalDate.of(2025, 4, 20))
                .designation("Pots verre 1kg x50")
                .montant(new BigDecimal("28.00"))
                .categorie(CategorieAchat.POTS)
                .build();
    }

    @Test
    void findAll_shouldReturnAllAchatsSortedByDateDesc() {
        // Given
        when(achatRepository.findAllByOrderByDateAchatDesc()).thenReturn(List.of(achatPots, achatCire));

        // When
        List<Achat> result = achatService.findAll();

        // Then
        assertThat(result).hasSize(2);
        verify(achatRepository).findAllByOrderByDateAchatDesc();
    }

    @Test
    void findById_shouldReturnAchat_whenExists() {
        // Given
        when(achatRepository.findById(1L)).thenReturn(Optional.of(achatCire));

        // When
        Achat result = achatService.findById(1L);

        // Then
        assertThat(result.getDesignation()).isEqualTo("Cire gaufrée 5kg");
        verify(achatRepository).findById(1L);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        // Given
        when(achatRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> achatService.findById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Achat #99 introuvable");
    }

    @Test
    void save_shouldDelegateToRepository() {
        // Given
        when(achatRepository.save(achatCire)).thenReturn(achatCire);

        // When
        Achat result = achatService.save(achatCire);

        // Then
        assertThat(result).isEqualTo(achatCire);
        verify(achatRepository).save(achatCire);
    }

    @Test
    void delete_shouldRemoveAchat_whenExists() {
        // Given
        when(achatRepository.findById(1L)).thenReturn(Optional.of(achatCire));

        // When
        achatService.delete(1L);

        // Then
        verify(achatRepository).delete(achatCire);
    }

    @Test
    void delete_shouldThrowException_whenNotFound() {
        // Given
        when(achatRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> achatService.delete(99L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findByPeriod_shouldReturnAchatsInRange() {
        // Given
        LocalDate start = LocalDate.of(2025, 4, 1);
        LocalDate end = LocalDate.of(2025, 4, 30);
        when(achatRepository.findByDateAchatBetween(start, end)).thenReturn(List.of(achatCire, achatPots));

        // When
        List<Achat> result = achatService.findByPeriod(start, end);

        // Then
        assertThat(result).hasSize(2);
        verify(achatRepository).findByDateAchatBetween(start, end);
    }

    @Test
    void findByCategorie_shouldReturnMatchingAchats() {
        // Given
        when(achatRepository.findByCategorie(CategorieAchat.CIRE)).thenReturn(List.of(achatCire));

        // When
        List<Achat> result = achatService.findByCategorie(CategorieAchat.CIRE);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getCategorie()).isEqualTo(CategorieAchat.CIRE);
    }

    @Test
    void calculateTotalDepenses_shouldSumAllMontantsInPeriod() {
        // Given
        LocalDate start = LocalDate.of(2025, 4, 1);
        LocalDate end = LocalDate.of(2025, 4, 30);
        when(achatRepository.findByDateAchatBetween(start, end))
                .thenReturn(List.of(achatCire, achatPots, achatPots2));

        // When
        BigDecimal total = achatService.calculateTotalDepenses(start, end);

        // Then - 45.00 + 32.50 + 28.00 = 105.50
        assertThat(total).isEqualByComparingTo("105.50");
    }

    @Test
    void calculateTotalDepenses_shouldReturnZero_whenNoPurchasesInPeriod() {
        // Given
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2020, 12, 31);
        when(achatRepository.findByDateAchatBetween(start, end)).thenReturn(Collections.emptyList());

        // When
        BigDecimal total = achatService.calculateTotalDepenses(start, end);

        // Then
        assertThat(total).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void calculateDepensesByCategorie_shouldGroupAndSumByCategory() {
        // Given
        LocalDate start = LocalDate.of(2025, 4, 1);
        LocalDate end = LocalDate.of(2025, 4, 30);
        when(achatRepository.findByDateAchatBetween(start, end))
                .thenReturn(List.of(achatCire, achatPots, achatPots2));

        // When
        Map<CategorieAchat, BigDecimal> result = achatService.calculateDepensesByCategorie(start, end);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(CategorieAchat.CIRE)).isEqualByComparingTo("45.00");
        assertThat(result.get(CategorieAchat.POTS)).isEqualByComparingTo("60.50"); // 32.50 + 28.00
    }

    @Test
    void calculateDepensesByCategorie_shouldReturnEmptyMap_whenNoPurchases() {
        // Given
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2020, 12, 31);
        when(achatRepository.findByDateAchatBetween(start, end)).thenReturn(Collections.emptyList());

        // When
        Map<CategorieAchat, BigDecimal> result = achatService.calculateDepensesByCategorie(start, end);

        // Then
        assertThat(result).isEmpty();
    }
}
