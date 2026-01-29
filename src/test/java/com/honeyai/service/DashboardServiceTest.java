package com.honeyai.service;

import com.honeyai.dto.TopProduitDto;
import com.honeyai.enums.HoneyType;
import com.honeyai.model.Order;
import com.honeyai.model.OrderLine;
import com.honeyai.model.Product;
import com.honeyai.repository.OrderRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AchatService achatService;

    @InjectMocks
    private DashboardService dashboardService;

    private static final LocalDate START = LocalDate.of(2025, 1, 1);
    private static final LocalDate END = LocalDate.of(2025, 12, 31);

    private Product produitFleurs;
    private Product produitForet;

    @BeforeEach
    void setUp() {
        produitFleurs = Product.builder()
                .id(1L)
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot")
                .build();

        produitForet = Product.builder()
                .id(2L)
                .name("Miel de Forêt 1kg")
                .type(HoneyType.FORET)
                .unit("pot")
                .build();
    }

    @Test
    void calculateChiffreAffaires_shouldSumOnlyPaidOrders() {
        // Given - 2 paid orders with lines
        Order order1 = buildOrderWithLines(
                buildLine(produitFleurs, 5, "8.50"),
                buildLine(produitForet, 2, "15.00"));
        Order order2 = buildOrderWithLines(
                buildLine(produitFleurs, 3, "8.50"));

        when(orderRepository.findPaidOrdersWithLinesBetween(START, END))
                .thenReturn(List.of(order1, order2));

        // When
        BigDecimal ca = dashboardService.calculateChiffreAffaires(START, END);

        // Then - (5*8.50) + (2*15.00) + (3*8.50) = 42.50 + 30.00 + 25.50 = 98.00
        assertThat(ca).isEqualByComparingTo("98.00");
        verify(orderRepository).findPaidOrdersWithLinesBetween(START, END);
    }

    @Test
    void calculateChiffreAffaires_shouldReturnZero_whenNoPaidOrders() {
        // Given
        when(orderRepository.findPaidOrdersWithLinesBetween(START, END))
                .thenReturn(Collections.emptyList());

        // When
        BigDecimal ca = dashboardService.calculateChiffreAffaires(START, END);

        // Then
        assertThat(ca).isEqualByComparingTo("0.00");
    }

    @Test
    void calculateTotalDepenses_shouldDelegateToAchatService() {
        // Given
        when(achatService.calculateTotalDepenses(START, END)).thenReturn(new BigDecimal("150.00"));

        // When
        BigDecimal depenses = dashboardService.calculateTotalDepenses(START, END);

        // Then
        assertThat(depenses).isEqualByComparingTo("150.00");
        verify(achatService).calculateTotalDepenses(START, END);
    }

    @Test
    void calculateBenefice_shouldReturnCaMinusDepenses() {
        // Given
        Order order = buildOrderWithLines(
                buildLine(produitFleurs, 10, "8.50")); // CA = 85.00
        when(orderRepository.findPaidOrdersWithLinesBetween(START, END))
                .thenReturn(List.of(order));
        when(achatService.calculateTotalDepenses(START, END))
                .thenReturn(new BigDecimal("30.00"));

        // When
        BigDecimal benefice = dashboardService.calculateBenefice(START, END);

        // Then - 85.00 - 30.00 = 55.00
        assertThat(benefice).isEqualByComparingTo("55.00");
    }

    @Test
    void calculateBenefice_shouldReturnNegative_whenDepensesExceedCa() {
        // Given
        when(orderRepository.findPaidOrdersWithLinesBetween(START, END))
                .thenReturn(Collections.emptyList());
        when(achatService.calculateTotalDepenses(START, END))
                .thenReturn(new BigDecimal("200.00"));

        // When
        BigDecimal benefice = dashboardService.calculateBenefice(START, END);

        // Then
        assertThat(benefice).isEqualByComparingTo("-200.00");
    }

    @Test
    void getTopProduits_shouldGroupByProductAndSortByQuantity() {
        // Given - produitFleurs: 5+3=8 qty, produitForet: 2 qty
        Order order1 = buildOrderWithLines(
                buildLine(produitFleurs, 5, "8.50"),
                buildLine(produitForet, 2, "15.00"));
        Order order2 = buildOrderWithLines(
                buildLine(produitFleurs, 3, "8.50"));

        when(orderRepository.findPaidOrdersWithLinesBetween(START, END))
                .thenReturn(List.of(order1, order2));

        // When
        List<TopProduitDto> top = dashboardService.getTopProduits(START, END, 10);

        // Then
        assertThat(top).hasSize(2);
        assertThat(top.get(0).getProduitNom()).isEqualTo("Miel Toutes Fleurs 500g");
        assertThat(top.get(0).getQuantiteTotale()).isEqualTo(8L);
        assertThat(top.get(0).getChiffreAffaires()).isEqualByComparingTo("68.00"); // 8 * 8.50
        assertThat(top.get(0).getTypeMiel()).isEqualTo("Toutes Fleurs");

        assertThat(top.get(1).getProduitNom()).isEqualTo("Miel de Forêt 1kg");
        assertThat(top.get(1).getQuantiteTotale()).isEqualTo(2L);
        assertThat(top.get(1).getChiffreAffaires()).isEqualByComparingTo("30.00");
    }

    @Test
    void getTopProduits_shouldLimitResults() {
        // Given
        Order order = buildOrderWithLines(
                buildLine(produitFleurs, 5, "8.50"),
                buildLine(produitForet, 2, "15.00"));

        when(orderRepository.findPaidOrdersWithLinesBetween(START, END))
                .thenReturn(List.of(order));

        // When
        List<TopProduitDto> top = dashboardService.getTopProduits(START, END, 1);

        // Then
        assertThat(top).hasSize(1);
        assertThat(top.get(0).getProduitNom()).isEqualTo("Miel Toutes Fleurs 500g");
    }

    @Test
    void getTopProduits_shouldReturnEmptyList_whenNoOrders() {
        // Given
        when(orderRepository.findPaidOrdersWithLinesBetween(START, END))
                .thenReturn(Collections.emptyList());

        // When
        List<TopProduitDto> top = dashboardService.getTopProduits(START, END, 5);

        // Then
        assertThat(top).isEmpty();
    }

    private Order buildOrderWithLines(OrderLine... lines) {
        Order order = Order.builder().build();
        for (OrderLine line : lines) {
            order.addLigne(line);
        }
        return order;
    }

    private OrderLine buildLine(Product product, int quantity, String unitPrice) {
        return OrderLine.builder()
                .product(product)
                .quantity(quantity)
                .unitPrice(new BigDecimal(unitPrice))
                .build();
    }
}
