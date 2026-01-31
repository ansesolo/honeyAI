package com.honeyai.controller;

import com.honeyai.dto.ProductPriceDto;
import com.honeyai.enums.OrderStatus;
import com.honeyai.exception.InvalidStatusTransitionException;
import com.honeyai.model.Client;
import com.honeyai.model.Order;
import com.honeyai.model.Product;
import com.honeyai.service.ClientService;
import com.honeyai.service.OrderService;
import com.honeyai.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ProductService productService;

    private Client client;
    private Order order1;
    private Order order2;
    private Product product;
    private ProductPriceDto productPriceDto;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .id(1L)
                .name("Dupont Jean")
                .build();

        product = Product.builder()
                .id(1L)
                .name("Miel Toutes Fleurs 500g")
                .unit("pot 500g")
                .build();

        productPriceDto = ProductPriceDto.builder()
                .id(1L)
                .name("Miel Toutes Fleurs 500g")
                .unit("pot 500g")
                .price(new BigDecimal("12.50"))
                .build();

        order1 = Order.builder()
                      .id(1L)
                      .client(client)
                      .orderDate(LocalDate.of(2026, 1, 15))
                      .status(OrderStatus.ORDERED)
                      .lines(new ArrayList<>())
                      .build();

        order2 = Order.builder()
                      .id(2L)
                      .client(client)
                      .orderDate(LocalDate.of(2026, 1, 10))
                      .status(OrderStatus.PAID)
                      .lines(new ArrayList<>())
                      .build();
    }

    @Test
    void list_shouldReturnOrdersListView() throws Exception {
        // Given
        when(orderService.findWithFilters(any(), any()))
                .thenReturn(Arrays.asList(order1, order2));
        when(orderService.getDistinctYears()).thenReturn(Arrays.asList(2026, 2025));
        when(orderService.calculateTotal(1L)).thenReturn(new BigDecimal("45.50"));
        when(orderService.calculateTotal(2L)).thenReturn(new BigDecimal("120.00"));

        // When/Then
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/list"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("currentYear"))
                .andExpect(model().attributeExists("years"))
                .andExpect(model().attributeExists("status"))
                .andExpect(model().attribute("activeMenu", "orders"));

        verify(orderService).findWithFilters(any(), any());
        verify(orderService).getDistinctYears();
    }

    @Test
    void list_withYearFilter_shouldFilterByYear() throws Exception {
        // Given
        when(orderService.findWithFilters(eq(2025), any()))
                .thenReturn(Collections.emptyList());
        when(orderService.getDistinctYears()).thenReturn(Arrays.asList(2026, 2025));

        // When/Then
        mockMvc.perform(get("/orders").param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/list"))
                .andExpect(model().attribute("selectedYear", 2025));

        verify(orderService).findWithFilters(eq(2025), isNull());
    }

    @Test
    void list_withStatusFilter_shouldFilterByStatus() throws Exception {
        // Given
        when(orderService.findWithFilters(any(), eq(OrderStatus.ORDERED)))
                .thenReturn(List.of(order1));
        when(orderService.getDistinctYears()).thenReturn(List.of(2026));
        when(orderService.calculateTotal(1L)).thenReturn(new BigDecimal("45.50"));

        // When/Then
        mockMvc.perform(get("/orders").param("status", "ORDERED"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/list"))
                .andExpect(model().attribute("selectedStatus", OrderStatus.ORDERED));

        verify(orderService).findWithFilters(isNull(), eq(OrderStatus.ORDERED));
    }

    @Test
    void list_withBothFilters_shouldFilterByYearAndStatus() throws Exception {
        // Given
        when(orderService.findWithFilters(eq(2026), eq(OrderStatus.PAID)))
                .thenReturn(List.of(order2));
        when(orderService.getDistinctYears()).thenReturn(List.of(2026));
        when(orderService.calculateTotal(2L)).thenReturn(new BigDecimal("120.00"));

        // When/Then
        mockMvc.perform(get("/orders")
                        .param("year", "2026")
                        .param("status", "PAID"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/list"))
                .andExpect(model().attribute("selectedYear", 2026))
                .andExpect(model().attribute("selectedStatus", OrderStatus.PAID));

        verify(orderService).findWithFilters(eq(2026), eq(OrderStatus.PAID));
    }

    @Test
    void list_shouldIncludeCurrentYearInYearsListEvenIfNoOrders() throws Exception {
        // Given
        int currentYear = LocalDate.now().getYear();
        when(orderService.findWithFilters(any(), any()))
                .thenReturn(Collections.emptyList());
        when(orderService.getDistinctYears()).thenReturn(Collections.emptyList());

        // When/Then
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("currentYear", currentYear));

        verify(orderService).findWithFilters(any(), any());
    }

    @Test
    void showCreateForm_shouldReturnFormView() throws Exception {
        // Given
        when(clientService.findAllActive()).thenReturn(List.of(client));
        when(productService.findAllWithCurrentYearPrices()).thenReturn(List.of(productPriceDto));

        // When/Then
        mockMvc.perform(get("/orders/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/form"))
                .andExpect(model().attributeExists("orderForm"))
                .andExpect(model().attributeExists("clients"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("activeMenu", "orders"));

        verify(clientService).findAllActive();
        verify(productService).findAllWithCurrentYearPrices();
    }

    @Test
    void showCreateForm_withClientIdParam_shouldPreSelectClient() throws Exception {
        // Given
        when(clientService.findAllActive()).thenReturn(List.of(client));
        when(productService.findAllWithCurrentYearPrices()).thenReturn(List.of(productPriceDto));

        // When/Then
        mockMvc.perform(get("/orders/new").param("clientId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/form"))
                .andExpect(model().attributeExists("orderForm"));
    }

    @Test
    void createOrder_withValidData_shouldRedirectToDetail() throws Exception {
        // Given
        when(clientService.findByIdOrThrow(1L)).thenReturn(client);
        when(productService.findById(1L)).thenReturn(Optional.of(product));
        when(orderService.create(any(Order.class))).thenReturn(order1);

        // When/Then
        mockMvc.perform(post("/orders")
                        .param("clientId", "1")
                        .param("orderDate", "2026-01-15")
                        .param("lines[0].productId", "1")
                        .param("lines[0].quantity", "2")
                        .param("lines[0].unitPrice", "12.50"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/1"))
                .andExpect(flash().attribute("successMessage", "Commande creee avec succes"));

        verify(orderService).create(any(Order.class));
    }

    @Test
    void createOrder_withoutClient_shouldReturnFormWithErrors() throws Exception {
        // Given
        when(clientService.findAllActive()).thenReturn(List.of(client));
        when(productService.findAllWithCurrentYearPrices()).thenReturn(List.of(productPriceDto));

        // When/Then
        mockMvc.perform(post("/orders")
                        .param("orderDate", "2026-01-15")
                        .param("lines[0].productId", "1")
                        .param("lines[0].quantity", "2")
                        .param("lines[0].unitPrice", "12.50"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/form"))
                .andExpect(model().hasErrors());

        verify(orderService, never()).create(any());
    }

    @Test
    void createOrder_withoutLignes_shouldReturnFormWithErrors() throws Exception {
        // Given
        when(clientService.findAllActive()).thenReturn(List.of(client));
        when(productService.findAllWithCurrentYearPrices()).thenReturn(List.of(productPriceDto));

        // When/Then
        mockMvc.perform(post("/orders")
                        .param("clientId", "1")
                        .param("orderDate", "2026-01-15"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/form"))
                .andExpect(model().hasErrors());

        verify(orderService, never()).create(any());
    }

    @Test
    void showDetail_shouldReturnDetailView() throws Exception {
        // Given - use order without ligne to avoid Lombok toString() recursion
        when(orderService.findById(1L)).thenReturn(Optional.of(order1));
        when(orderService.calculateTotal(1L)).thenReturn(new BigDecimal("25.00"));

        // When/Then
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/detail"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("total"))
                .andExpect(model().attribute("activeMenu", "orders"));

        verify(orderService).findById(1L);
        verify(orderService).calculateTotal(1L);
    }

    @Test
    void updateStatus_shouldRedirectWithSuccess() throws Exception {
        // Given
        Order updated = Order.builder()
                             .id(1L)
                             .client(client)
                             .status(OrderStatus.RECOVERED)
                             .lines(new ArrayList<>())
                             .build();
        when(orderService.updateStatus(1L, OrderStatus.RECOVERED)).thenReturn(updated);

        // When/Then
        mockMvc.perform(post("/orders/1/status")
                        .param("newStatus", "RECOVERED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/1"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(orderService).updateStatus(1L, OrderStatus.RECOVERED);
    }

    @Test
    void updateStatus_withInvalidTransition_shouldRedirectWithError() throws Exception {
        // Given
        when(orderService.updateStatus(1L, OrderStatus.PAID))
                .thenThrow(new InvalidStatusTransitionException(OrderStatus.ORDERED, OrderStatus.PAID));

        // When/Then
        mockMvc.perform(post("/orders/1/status")
                        .param("newStatus", "PAID"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/1"))
                .andExpect(flash().attribute("errorMessage", "Transition invalide"));

        verify(orderService).updateStatus(1L, OrderStatus.PAID);
    }

    @Test
    void showEditForm_shouldReturnFormViewWithOrderData() throws Exception {
        // Given
        when(orderService.findById(1L)).thenReturn(Optional.of(order1));
        when(clientService.findAllActive()).thenReturn(List.of(client));
        when(productService.findAllWithCurrentYearPrices()).thenReturn(List.of(productPriceDto));

        // When/Then
        mockMvc.perform(get("/orders/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/form"))
                .andExpect(model().attributeExists("orderForm"))
                .andExpect(model().attribute("orderId", 1L))
                .andExpect(model().attribute("isEdit", true));

        verify(orderService).findById(1L);
    }

    @Test
    void updateOrder_withValidData_shouldRedirectToDetail() throws Exception {
        // Given
        when(orderService.findById(1L)).thenReturn(Optional.of(order1));
        when(clientService.findByIdOrThrow(1L)).thenReturn(client);
        when(productService.findById(1L)).thenReturn(Optional.of(product));
        when(orderService.save(any(Order.class))).thenReturn(order1);

        // When/Then
        mockMvc.perform(post("/orders/1/edit")
                        .param("clientId", "1")
                        .param("orderDate", "2026-01-15")
                        .param("lines[0].productId", "1")
                        .param("lines[0].quantity", "3")
                        .param("lines[0].unitPrice", "12.50"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/1"))
                .andExpect(flash().attribute("successMessage", "Commande modifiee avec succes"));

        verify(orderService).save(any(Order.class));
    }
}
