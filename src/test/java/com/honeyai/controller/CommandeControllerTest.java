package com.honeyai.controller;

import com.honeyai.dto.ProductPriceDto;
import com.honeyai.enums.StatutCommande;
import com.honeyai.exception.InvalidStatusTransitionException;
import com.honeyai.model.Client;
import com.honeyai.model.Commande;
import com.honeyai.model.LigneCommande;
import com.honeyai.model.Product;
import com.honeyai.service.ClientService;
import com.honeyai.service.CommandeService;
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

@WebMvcTest(CommandeController.class)
class CommandeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandeService commandeService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ProductService productService;

    private Client client;
    private Commande commande1;
    private Commande commande2;
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

        commande1 = Commande.builder()
                .id(1L)
                .client(client)
                .dateCommande(LocalDate.of(2026, 1, 15))
                .statut(StatutCommande.COMMANDEE)
                .lignes(new ArrayList<>())
                .build();

        commande2 = Commande.builder()
                .id(2L)
                .client(client)
                .dateCommande(LocalDate.of(2026, 1, 10))
                .statut(StatutCommande.PAYEE)
                .lignes(new ArrayList<>())
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

    @Test
    void showCreateForm_shouldReturnFormView() throws Exception {
        // Given
        when(clientService.findAllActive()).thenReturn(List.of(client));
        when(productService.findAllWithCurrentYearPrices()).thenReturn(List.of(productPriceDto));

        // When/Then
        mockMvc.perform(get("/commandes/nouvelle"))
                .andExpect(status().isOk())
                .andExpect(view().name("commandes/form"))
                .andExpect(model().attributeExists("commandeForm"))
                .andExpect(model().attributeExists("clients"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("activeMenu", "commandes"));

        verify(clientService).findAllActive();
        verify(productService).findAllWithCurrentYearPrices();
    }

    @Test
    void showCreateForm_withClientIdParam_shouldPreSelectClient() throws Exception {
        // Given
        when(clientService.findAllActive()).thenReturn(List.of(client));
        when(productService.findAllWithCurrentYearPrices()).thenReturn(List.of(productPriceDto));

        // When/Then
        mockMvc.perform(get("/commandes/nouvelle").param("clientId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("commandes/form"))
                .andExpect(model().attributeExists("commandeForm"));
    }

    @Test
    void createCommande_withValidData_shouldRedirectToDetail() throws Exception {
        // Given
        when(clientService.findByIdOrThrow(1L)).thenReturn(client);
        when(productService.findById(1L)).thenReturn(Optional.of(product));
        when(commandeService.create(any(Commande.class))).thenReturn(commande1);

        // When/Then
        mockMvc.perform(post("/commandes")
                        .param("clientId", "1")
                        .param("dateCommande", "2026-01-15")
                        .param("lignes[0].productId", "1")
                        .param("lignes[0].quantite", "2")
                        .param("lignes[0].prixUnitaire", "12.50"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/commandes/1"))
                .andExpect(flash().attribute("success", "Commande creee avec succes"));

        verify(commandeService).create(any(Commande.class));
    }

    @Test
    void createCommande_withoutClient_shouldReturnFormWithErrors() throws Exception {
        // Given
        when(clientService.findAllActive()).thenReturn(List.of(client));
        when(productService.findAllWithCurrentYearPrices()).thenReturn(List.of(productPriceDto));

        // When/Then
        mockMvc.perform(post("/commandes")
                        .param("dateCommande", "2026-01-15")
                        .param("lignes[0].productId", "1")
                        .param("lignes[0].quantite", "2")
                        .param("lignes[0].prixUnitaire", "12.50"))
                .andExpect(status().isOk())
                .andExpect(view().name("commandes/form"))
                .andExpect(model().hasErrors());

        verify(commandeService, never()).create(any());
    }

    @Test
    void createCommande_withoutLignes_shouldReturnFormWithErrors() throws Exception {
        // Given
        when(clientService.findAllActive()).thenReturn(List.of(client));
        when(productService.findAllWithCurrentYearPrices()).thenReturn(List.of(productPriceDto));

        // When/Then
        mockMvc.perform(post("/commandes")
                        .param("clientId", "1")
                        .param("dateCommande", "2026-01-15"))
                .andExpect(status().isOk())
                .andExpect(view().name("commandes/form"))
                .andExpect(model().hasErrors());

        verify(commandeService, never()).create(any());
    }

    @Test
    void showDetail_shouldReturnDetailView() throws Exception {
        // Given - use commande without ligne to avoid Lombok toString() recursion
        when(commandeService.findById(1L)).thenReturn(Optional.of(commande1));
        when(commandeService.calculateTotal(1L)).thenReturn(new BigDecimal("25.00"));

        // When/Then
        mockMvc.perform(get("/commandes/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("commandes/detail"))
                .andExpect(model().attributeExists("commande"))
                .andExpect(model().attributeExists("total"))
                .andExpect(model().attribute("activeMenu", "commandes"));

        verify(commandeService).findById(1L);
        verify(commandeService).calculateTotal(1L);
    }

    @Test
    void updateStatut_shouldRedirectWithSuccess() throws Exception {
        // Given
        Commande updated = Commande.builder()
                .id(1L)
                .client(client)
                .statut(StatutCommande.RECUPEREE)
                .lignes(new ArrayList<>())
                .build();
        when(commandeService.updateStatut(1L, StatutCommande.RECUPEREE)).thenReturn(updated);

        // When/Then
        mockMvc.perform(post("/commandes/1/statut")
                        .param("newStatut", "RECUPEREE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/commandes/1"))
                .andExpect(flash().attributeExists("success"));

        verify(commandeService).updateStatut(1L, StatutCommande.RECUPEREE);
    }

    @Test
    void updateStatut_withInvalidTransition_shouldRedirectWithError() throws Exception {
        // Given
        when(commandeService.updateStatut(1L, StatutCommande.PAYEE))
                .thenThrow(new InvalidStatusTransitionException(StatutCommande.COMMANDEE, StatutCommande.PAYEE));

        // When/Then
        mockMvc.perform(post("/commandes/1/statut")
                        .param("newStatut", "PAYEE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/commandes/1"))
                .andExpect(flash().attribute("error", "Transition invalide"));

        verify(commandeService).updateStatut(1L, StatutCommande.PAYEE);
    }

    @Test
    void showEditForm_shouldReturnFormViewWithCommandeData() throws Exception {
        // Given
        when(commandeService.findById(1L)).thenReturn(Optional.of(commande1));
        when(clientService.findAllActive()).thenReturn(List.of(client));
        when(productService.findAllWithCurrentYearPrices()).thenReturn(List.of(productPriceDto));

        // When/Then
        mockMvc.perform(get("/commandes/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("commandes/form"))
                .andExpect(model().attributeExists("commandeForm"))
                .andExpect(model().attribute("commandeId", 1L))
                .andExpect(model().attribute("isEdit", true));

        verify(commandeService).findById(1L);
    }

    @Test
    void updateCommande_withValidData_shouldRedirectToDetail() throws Exception {
        // Given
        when(commandeService.findById(1L)).thenReturn(Optional.of(commande1));
        when(clientService.findByIdOrThrow(1L)).thenReturn(client);
        when(productService.findById(1L)).thenReturn(Optional.of(product));
        when(commandeService.save(any(Commande.class))).thenReturn(commande1);

        // When/Then
        mockMvc.perform(post("/commandes/1/edit")
                        .param("clientId", "1")
                        .param("dateCommande", "2026-01-15")
                        .param("lignes[0].productId", "1")
                        .param("lignes[0].quantite", "3")
                        .param("lignes[0].prixUnitaire", "12.50"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/commandes/1"))
                .andExpect(flash().attribute("success", "Commande modifiee avec succes"));

        verify(commandeService).save(any(Commande.class));
    }
}
