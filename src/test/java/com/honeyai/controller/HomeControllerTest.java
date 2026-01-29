package com.honeyai.controller;

import com.honeyai.dto.TopProduitDto;
import com.honeyai.enums.CategorieAchat;
import com.honeyai.enums.OrderStatus;
import com.honeyai.model.Order;
import com.honeyai.repository.OrderRepository;
import com.honeyai.service.AchatService;
import com.honeyai.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @MockBean
    private AchatService achatService;

    @MockBean
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        // Default stubs for all tests
        when(dashboardService.calculateChiffreAffaires(any(), any()))
                .thenReturn(BigDecimal.ZERO);
        when(dashboardService.calculateTotalDepenses(any(), any()))
                .thenReturn(BigDecimal.ZERO);
        when(dashboardService.calculateBenefice(any(), any()))
                .thenReturn(BigDecimal.ZERO);
        when(dashboardService.getTopProduits(any(), any(), anyInt()))
                .thenReturn(Collections.emptyList());
        when(achatService.calculateDepensesByCategorie(any(), any()))
                .thenReturn(Collections.emptyMap());
        when(orderRepository.findByStatus(OrderStatus.PAID))
                .thenReturn(Collections.emptyList());
    }

    @Test
    void home_shouldReturnDashboardView_withDefaultYear() throws Exception {
        // Given
        when(dashboardService.calculateChiffreAffaires(any(), any()))
                .thenReturn(new BigDecimal("500.00"));
        when(dashboardService.calculateTotalDepenses(any(), any()))
                .thenReturn(new BigDecimal("150.00"));
        when(dashboardService.calculateBenefice(any(), any()))
                .thenReturn(new BigDecimal("350.00"));

        // When / Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("ca", "depenses", "benefice",
                        "commandesPayees", "topProduits", "depensesParCategorie",
                        "selectedYear", "availableYears"))
                .andExpect(model().attribute("activeMenu", "dashboard"))
                .andExpect(model().attribute("ca", comparesEqualTo(new BigDecimal("500.00"))))
                .andExpect(model().attribute("depenses", comparesEqualTo(new BigDecimal("150.00"))))
                .andExpect(model().attribute("benefice", comparesEqualTo(new BigDecimal("350.00"))));
    }

    @Test
    void home_shouldFilterByYear() throws Exception {
        // Given
        when(dashboardService.calculateChiffreAffaires(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31)))
                .thenReturn(new BigDecimal("200.00"));

        // When / Then
        mockMvc.perform(get("/").param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedYear", 2025))
                .andExpect(model().attribute("ca", comparesEqualTo(new BigDecimal("200.00"))));
    }

    @Test
    void home_shouldCountPaidOrdersInYear() throws Exception {
        // Given
        Order paidInYear = Order.builder()
                .orderDate(LocalDate.of(2025, 6, 15))
                .status(OrderStatus.PAID)
                .build();
        Order paidOutOfYear = Order.builder()
                .orderDate(LocalDate.of(2024, 3, 1))
                .status(OrderStatus.PAID)
                .build();

        when(orderRepository.findByStatus(OrderStatus.PAID))
                .thenReturn(List.of(paidInYear, paidOutOfYear));

        // When / Then
        mockMvc.perform(get("/").param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("commandesPayees", 1L));
    }

    @Test
    void home_shouldShowZeroMetrics_whenNoData() throws Exception {
        // When / Then (all defaults from setUp)
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("ca", comparesEqualTo(BigDecimal.ZERO)))
                .andExpect(model().attribute("commandesPayees", 0L))
                .andExpect(model().attribute("topProduits", empty()));
    }

    @Test
    void home_shouldIncludeTopProduits() throws Exception {
        // Given
        List<TopProduitDto> topProduits = List.of(
                TopProduitDto.builder()
                        .produitNom("Miel Toutes Fleurs 500g")
                        .typeMiel("Toutes Fleurs")
                        .quantiteTotale(20L)
                        .chiffreAffaires(new BigDecimal("170.00"))
                        .build(),
                TopProduitDto.builder()
                        .produitNom("Miel de Foret 1kg")
                        .typeMiel("Foret")
                        .quantiteTotale(10L)
                        .chiffreAffaires(new BigDecimal("150.00"))
                        .build()
        );
        when(dashboardService.getTopProduits(any(), any(), anyInt()))
                .thenReturn(topProduits);

        // When / Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("topProduits", hasSize(2)));
    }

    @Test
    void home_shouldShowEmptyTopProduits_whenNoSales() throws Exception {
        // When / Then (default from setUp returns empty list)
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("topProduits", empty()));
    }

    @Test
    void home_shouldIncludeDepensesParCategorie() throws Exception {
        // Given
        when(dashboardService.calculateTotalDepenses(any(), any()))
                .thenReturn(new BigDecimal("100.00"));
        when(achatService.calculateDepensesByCategorie(any(), any()))
                .thenReturn(Map.of(
                        CategorieAchat.CIRE, new BigDecimal("60.00"),
                        CategorieAchat.POTS, new BigDecimal("40.00")));

        // When / Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("depensesParCategorie", hasSize(2)));
    }

    @Test
    void home_shouldShowEmptyDepenses_whenNoExpenses() throws Exception {
        // When / Then (default from setUp returns empty map)
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("depensesParCategorie", empty()));
    }
}
