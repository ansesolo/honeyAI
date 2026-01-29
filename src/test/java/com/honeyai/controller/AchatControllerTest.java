package com.honeyai.controller;

import com.honeyai.enums.CategorieAchat;
import com.honeyai.model.Achat;
import com.honeyai.service.AchatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AchatController.class)
class AchatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AchatService achatService;

    @Test
    void list_shouldReturnAchatsListView() throws Exception {
        // Given
        Achat achat = Achat.builder()
                .id(1L)
                .dateAchat(LocalDate.of(2025, 4, 10))
                .designation("Cire gaufrée")
                .montant(new BigDecimal("45.00"))
                .categorie(CategorieAchat.CIRE)
                .build();
        when(achatService.findAll()).thenReturn(List.of(achat));

        // When / Then
        mockMvc.perform(get("/achats"))
                .andExpect(status().isOk())
                .andExpect(view().name("achats/list"))
                .andExpect(model().attributeExists("achats", "total", "categories", "achat"))
                .andExpect(model().attribute("activeMenu", "achats"))
                .andExpect(model().attribute("achats", hasSize(1)));
    }

    @Test
    void list_shouldShowEmptyState_whenNoAchats() throws Exception {
        // Given
        when(achatService.findAll()).thenReturn(Collections.emptyList());

        // When / Then
        mockMvc.perform(get("/achats"))
                .andExpect(status().isOk())
                .andExpect(view().name("achats/list"))
                .andExpect(model().attribute("achats", empty()));
    }

    @Test
    void list_shouldFilterByYear() throws Exception {
        // Given
        Achat achat = Achat.builder()
                .id(1L)
                .dateAchat(LocalDate.of(2025, 6, 1))
                .designation("Pots")
                .montant(new BigDecimal("30.00"))
                .categorie(CategorieAchat.POTS)
                .build();
        when(achatService.findByPeriod(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)))
                .thenReturn(List.of(achat));

        // When / Then
        mockMvc.perform(get("/achats").param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedYear", 2025))
                .andExpect(model().attribute("achats", hasSize(1)));
    }

    @Test
    void list_shouldFilterByCategorie() throws Exception {
        // Given
        Achat achat = Achat.builder()
                .id(1L)
                .dateAchat(LocalDate.of(2025, 4, 10))
                .designation("Cire")
                .montant(new BigDecimal("45.00"))
                .categorie(CategorieAchat.CIRE)
                .build();
        when(achatService.findByCategorie(CategorieAchat.CIRE)).thenReturn(List.of(achat));

        // When / Then
        mockMvc.perform(get("/achats").param("categorie", "CIRE"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedCategorie", CategorieAchat.CIRE))
                .andExpect(model().attribute("achats", hasSize(1)));
    }

    @Test
    void list_shouldCalculateTotal() throws Exception {
        // Given
        List<Achat> achats = List.of(
                Achat.builder().id(1L).dateAchat(LocalDate.now()).designation("A")
                        .montant(new BigDecimal("10.00")).categorie(CategorieAchat.CIRE).build(),
                Achat.builder().id(2L).dateAchat(LocalDate.now()).designation("B")
                        .montant(new BigDecimal("20.50")).categorie(CategorieAchat.POTS).build()
        );
        when(achatService.findAll()).thenReturn(achats);

        // When / Then
        mockMvc.perform(get("/achats"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("total", comparesEqualTo(new BigDecimal("30.50"))));
    }

    @Test
    void save_shouldRedirectOnSuccess() throws Exception {
        // Given
        Achat saved = Achat.builder()
                .id(1L)
                .dateAchat(LocalDate.of(2025, 4, 10))
                .designation("Cire")
                .montant(new BigDecimal("45.00"))
                .categorie(CategorieAchat.CIRE)
                .build();
        when(achatService.save(any(Achat.class))).thenReturn(saved);

        // When / Then
        mockMvc.perform(post("/achats")
                        .param("dateAchat", "2025-04-10")
                        .param("designation", "Cire gaufrée")
                        .param("montant", "45.00")
                        .param("categorie", "CIRE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/achats"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(achatService).save(any(Achat.class));
    }

    @Test
    void save_shouldReturnFormWithErrors_whenValidationFails() throws Exception {
        // Given - missing required fields
        when(achatService.findAll()).thenReturn(Collections.emptyList());

        // When / Then
        mockMvc.perform(post("/achats")
                        .param("designation", "")
                        .param("montant", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("achats/list"))
                .andExpect(model().attributeHasErrors("achat"));

        verify(achatService, never()).save(any(Achat.class));
    }

    @Test
    void editForm_shouldReturnFormView() throws Exception {
        // Given
        Achat achat = Achat.builder()
                .id(1L)
                .dateAchat(LocalDate.of(2025, 4, 10))
                .designation("Cire gaufrée")
                .montant(new BigDecimal("45.00"))
                .categorie(CategorieAchat.CIRE)
                .build();
        when(achatService.findById(1L)).thenReturn(achat);

        // When / Then
        mockMvc.perform(get("/achats/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("achats/form"))
                .andExpect(model().attributeExists("achat", "categories"))
                .andExpect(model().attribute("activeMenu", "achats"));
    }

    @Test
    void update_shouldRedirectOnSuccess() throws Exception {
        // Given
        Achat updated = Achat.builder()
                .id(1L)
                .dateAchat(LocalDate.of(2025, 5, 1))
                .designation("Cire modifiee")
                .montant(new BigDecimal("50.00"))
                .categorie(CategorieAchat.CIRE)
                .build();
        when(achatService.save(any(Achat.class))).thenReturn(updated);

        // When / Then
        mockMvc.perform(post("/achats/1")
                        .param("dateAchat", "2025-05-01")
                        .param("designation", "Cire modifiee")
                        .param("montant", "50.00")
                        .param("categorie", "CIRE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/achats"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(achatService).save(any(Achat.class));
    }

    @Test
    void update_shouldReturnFormWithErrors_whenValidationFails() throws Exception {
        // When / Then
        mockMvc.perform(post("/achats/1")
                        .param("designation", "")
                        .param("montant", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("achats/form"))
                .andExpect(model().attributeHasErrors("achat"));

        verify(achatService, never()).save(any(Achat.class));
    }

    @Test
    void delete_shouldRedirectWithSuccessMessage() throws Exception {
        // Given
        doNothing().when(achatService).delete(1L);

        // When / Then
        mockMvc.perform(post("/achats/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/achats"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(achatService).delete(1L);
    }
}
