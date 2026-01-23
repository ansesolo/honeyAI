package com.honeyai.controller;

import com.honeyai.enums.StatutCommande;
import com.honeyai.model.Client;
import com.honeyai.model.Commande;
import com.honeyai.service.CommandeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommandeController.class)
class CommandeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandeService commandeService;

    private Client client;
    private Commande commande1;
    private Commande commande2;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .id(1L)
                .name("Dupont Jean")
                .build();

        commande1 = Commande.builder()
                .id(1L)
                .client(client)
                .dateCommande(LocalDate.of(2026, 1, 15))
                .statut(StatutCommande.COMMANDEE)
                .build();

        commande2 = Commande.builder()
                .id(2L)
                .client(client)
                .dateCommande(LocalDate.of(2026, 1, 10))
                .statut(StatutCommande.PAYEE)
                .build();
    }

    @Test
    void list_shouldReturnCommandesListView() throws Exception {
        // Given
        when(commandeService.findWithFilters(any(), any()))
                .thenReturn(Arrays.asList(commande1, commande2));
        when(commandeService.getDistinctYears()).thenReturn(Arrays.asList(2026, 2025));
        when(commandeService.calculateTotal(1L)).thenReturn(new BigDecimal("45.50"));
        when(commandeService.calculateTotal(2L)).thenReturn(new BigDecimal("120.00"));

        // When/Then
        mockMvc.perform(get("/commandes"))
                .andExpect(status().isOk())
                .andExpect(view().name("commandes/list"))
                .andExpect(model().attributeExists("commandes"))
                .andExpect(model().attributeExists("currentYear"))
                .andExpect(model().attributeExists("years"))
                .andExpect(model().attributeExists("statuts"))
                .andExpect(model().attribute("activeMenu", "commandes"));

        verify(commandeService).findWithFilters(any(), any());
        verify(commandeService).getDistinctYears();
    }

    @Test
    void list_withYearFilter_shouldFilterByYear() throws Exception {
        // Given
        when(commandeService.findWithFilters(eq(2025), any()))
                .thenReturn(Collections.emptyList());
        when(commandeService.getDistinctYears()).thenReturn(Arrays.asList(2026, 2025));

        // When/Then
        mockMvc.perform(get("/commandes").param("annee", "2025"))
                .andExpect(status().isOk())
                .andExpect(view().name("commandes/list"))
                .andExpect(model().attribute("selectedYear", 2025));

        verify(commandeService).findWithFilters(eq(2025), isNull());
    }

    @Test
    void list_withStatutFilter_shouldFilterByStatut() throws Exception {
        // Given
        when(commandeService.findWithFilters(any(), eq(StatutCommande.COMMANDEE)))
                .thenReturn(List.of(commande1));
        when(commandeService.getDistinctYears()).thenReturn(Arrays.asList(2026));
        when(commandeService.calculateTotal(1L)).thenReturn(new BigDecimal("45.50"));

        // When/Then
        mockMvc.perform(get("/commandes").param("statut", "COMMANDEE"))
                .andExpect(status().isOk())
                .andExpect(view().name("commandes/list"))
                .andExpect(model().attribute("selectedStatut", StatutCommande.COMMANDEE));

        verify(commandeService).findWithFilters(isNull(), eq(StatutCommande.COMMANDEE));
    }

    @Test
    void list_withBothFilters_shouldFilterByYearAndStatut() throws Exception {
        // Given
        when(commandeService.findWithFilters(eq(2026), eq(StatutCommande.PAYEE)))
                .thenReturn(List.of(commande2));
        when(commandeService.getDistinctYears()).thenReturn(Arrays.asList(2026));
        when(commandeService.calculateTotal(2L)).thenReturn(new BigDecimal("120.00"));

        // When/Then
        mockMvc.perform(get("/commandes")
                        .param("annee", "2026")
                        .param("statut", "PAYEE"))
                .andExpect(status().isOk())
                .andExpect(view().name("commandes/list"))
                .andExpect(model().attribute("selectedYear", 2026))
                .andExpect(model().attribute("selectedStatut", StatutCommande.PAYEE));

        verify(commandeService).findWithFilters(eq(2026), eq(StatutCommande.PAYEE));
    }

    @Test
    void list_shouldIncludeCurrentYearInYearsListEvenIfNoOrders() throws Exception {
        // Given
        int currentYear = LocalDate.now().getYear();
        when(commandeService.findWithFilters(any(), any()))
                .thenReturn(Collections.emptyList());
        when(commandeService.getDistinctYears()).thenReturn(Collections.emptyList());

        // When/Then
        mockMvc.perform(get("/commandes"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("currentYear", currentYear));

        verify(commandeService).findWithFilters(any(), any());
    }
}
