package com.honeyai.controller;

import com.honeyai.enums.HoneyType;
import com.honeyai.model.Price;
import com.honeyai.model.Product;
import com.honeyai.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = Product.builder()
                .id(1L)
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot 500g")
                .build();

        product2 = Product.builder()
                .id(2L)
                .name("Miel Foret 1kg")
                .type(HoneyType.FORET)
                .unit("pot 1kg")
                .build();
    }

    @Test
    void list_shouldReturnProduitsListView() throws Exception {
        // Given
        when(productService.findAll()).thenReturn(Arrays.asList(product1, product2));
        when(productService.getPriceForYear(eq(1L), any())).thenReturn(new BigDecimal("8.00"));
        when(productService.getPriceForYear(eq(2L), any())).thenReturn(new BigDecimal("17.00"));

        // When/Then
        mockMvc.perform(get("/produits"))
                .andExpect(status().isOk())
                .andExpect(view().name("produits/list"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("currentYear"))
                .andExpect(model().attributeExists("productService"))
                .andExpect(model().attribute("activeMenu", "produits"));

        verify(productService).findAll();
    }

    @Test
    void updateTarif_shouldUpdatePriceAndRedirect() throws Exception {
        // Given
        when(productService.findById(1L)).thenReturn(Optional.of(product1));
        when(productService.updatePrice(eq(1L), eq(2026), any(BigDecimal.class)))
                .thenReturn(Price.builder()
                        .product(product1)
                        .year(2026)
                        .price(new BigDecimal("9.50"))
                        .build());

        // When/Then
        mockMvc.perform(post("/produits/1/tarif")
                        .param("annee", "2026")
                        .param("prix", "9.50"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produits"))
                .andExpect(flash().attributeExists("success"));

        verify(productService).updatePrice(eq(1L), eq(2026), eq(new BigDecimal("9.50")));
    }

    @Test
    void list_shouldDisplayCurrentYear() throws Exception {
        // Given
        when(productService.findAll()).thenReturn(Arrays.asList(product1));
        int expectedYear = LocalDate.now().getYear();

        // When/Then
        mockMvc.perform(get("/produits"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("currentYear", expectedYear));
    }
}
