package com.honeyai.controller;

import com.honeyai.config.EtiquetteConfig;
import com.honeyai.dto.EtiquetteData;
import com.honeyai.dto.EtiquetteRequest;
import com.honeyai.enums.FormatPot;
import com.honeyai.enums.HoneyType;
import com.honeyai.model.HistoriqueEtiquettes;
import com.honeyai.service.EtiquetteService;
import com.honeyai.service.PdfService;
import com.honeyai.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EtiquetteController.class)
class EtiquetteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EtiquetteService etiquetteService;

    @MockBean
    private PdfService pdfService;

    @MockBean
    private ProductService productService;

    @MockBean
    private EtiquetteConfig etiquetteConfig;

    // ==================== GET /etiquettes Tests ====================

    @Test
    void showForm_shouldReturnFormView() throws Exception {
        mockMvc.perform(get("/etiquettes"))
                .andExpect(status().isOk())
                .andExpect(view().name("etiquettes/form"));
    }

    @Test
    void showForm_shouldIncludeEtiquetteRequest() throws Exception {
        mockMvc.perform(get("/etiquettes"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("etiquetteRequest"))
                .andExpect(model().attribute("etiquetteRequest", instanceOf(EtiquetteRequest.class)));
    }

    @Test
    void showForm_shouldIncludeHoneyTypes() throws Exception {
        mockMvc.perform(get("/etiquettes"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("honeyTypes"))
                .andExpect(model().attribute("honeyTypes", HoneyType.values()));
    }

    @Test
    void showForm_shouldIncludeFormatPots() throws Exception {
        mockMvc.perform(get("/etiquettes"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("formatPots"))
                .andExpect(model().attribute("formatPots", FormatPot.values()));
    }

    @Test
    void showForm_shouldSetActiveMenu() throws Exception {
        mockMvc.perform(get("/etiquettes"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("activeMenu", "etiquettes"));
    }

    @Test
    void showForm_shouldHaveDefaultHoneyType() throws Exception {
        mockMvc.perform(get("/etiquettes"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("etiquetteRequest",
                        hasProperty("typeMiel", equalTo(HoneyType.TOUTES_FLEURS))));
    }

    @Test
    void showForm_shouldHaveDefaultFormatPot() throws Exception {
        mockMvc.perform(get("/etiquettes"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("etiquetteRequest",
                        hasProperty("formatPot", equalTo(FormatPot.POT_500G))));
    }

    @Test
    void showForm_shouldHaveTodayAsDefaultDate() throws Exception {
        mockMvc.perform(get("/etiquettes"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("etiquetteRequest",
                        hasProperty("dateRecolte", notNullValue())));
    }

    // ==================== POST /etiquettes/generer Tests ====================

    @Test
    void generatePdf_shouldReturnPdf_whenValid() throws Exception {
        // Given
        EtiquetteData mockData = EtiquetteData.builder()
                .typeMiel("Toutes Fleurs")
                .formatPot("500g")
                .numeroLot("2024-TF-001")
                .dluo(LocalDate.of(2026, 8, 15))
                .build();

        byte[] mockPdfBytes = "PDF content".getBytes();

        when(etiquetteService.buildEtiquetteData(any(EtiquetteRequest.class))).thenReturn(mockData);
        when(productService.findPriceByTypeAndFormat(any(), any())).thenReturn(new BigDecimal("8.50"));
        when(pdfService.generateEtiquetteSheet(any(EtiquetteData.class))).thenReturn(mockPdfBytes);
        when(pdfService.getLabelsPerPage()).thenReturn(21);

        // When/Then
        mockMvc.perform(post("/etiquettes/generer")
                        .param("typeMiel", "TOUTES_FLEURS")
                        .param("formatPot", "POT_500G")
                        .param("dateRecolte", "2024-08-15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition",
                        containsString("attachment; filename=")))
                .andExpect(header().string("Content-Disposition",
                        containsString("etiquettes-toutes-fleurs-2024-08-15-2024-TF-001.pdf")));

        verify(etiquetteService).buildEtiquetteData(any(EtiquetteRequest.class));
        verify(pdfService).generateEtiquetteSheet(any(EtiquetteData.class));
    }

    @Test
    void generatePdf_shouldReturnFormWithErrors_whenValidationFails() throws Exception {
        mockMvc.perform(post("/etiquettes/generer")
                        .param("typeMiel", "TOUTES_FLEURS")
                        .param("formatPot", "POT_500G"))
                // Missing dateRecolte -> validation error
                .andExpect(status().isOk())
                .andExpect(view().name("etiquettes/form"))
                .andExpect(model().attributeHasFieldErrors("etiquetteRequest", "dateRecolte"));

        verify(etiquetteService, never()).buildEtiquetteData(any());
    }

    @Test
    void generatePdf_shouldReturnFormWithError_whenServiceFails() throws Exception {
        // Given
        when(etiquetteService.buildEtiquetteData(any(EtiquetteRequest.class)))
                .thenThrow(new RuntimeException("Service error"));

        // When/Then
        mockMvc.perform(post("/etiquettes/generer")
                        .param("typeMiel", "TOUTES_FLEURS")
                        .param("formatPot", "POT_500G")
                        .param("dateRecolte", "2024-08-15"))
                .andExpect(status().isOk())
                .andExpect(view().name("etiquettes/form"))
                .andExpect(model().attribute("error", containsString("Erreur")));
    }

    @Test
    void generatePdf_shouldGenerateWithoutPrice_whenPriceNotFound() throws Exception {
        // Given
        EtiquetteData mockData = EtiquetteData.builder()
                .typeMiel("Toutes Fleurs")
                .formatPot("500g")
                .numeroLot("2024-TF-001")
                .dluo(LocalDate.of(2026, 8, 15))
                .build();

        byte[] mockPdfBytes = "PDF content".getBytes();

        when(etiquetteService.buildEtiquetteData(any(EtiquetteRequest.class))).thenReturn(mockData);
        when(productService.findPriceByTypeAndFormat(any(), any())).thenReturn(null); // No price
        when(pdfService.generateEtiquetteSheet(any(EtiquetteData.class))).thenReturn(mockPdfBytes);
        when(pdfService.getLabelsPerPage()).thenReturn(21);

        // When/Then
        mockMvc.perform(post("/etiquettes/generer")
                        .param("typeMiel", "TOUTES_FLEURS")
                        .param("formatPot", "POT_500G")
                        .param("dateRecolte", "2024-08-15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    void generatePdf_shouldRequireTypeMiel() throws Exception {
        mockMvc.perform(post("/etiquettes/generer")
                        .param("formatPot", "POT_500G")
                        .param("dateRecolte", "2024-08-15"))
                .andExpect(status().isOk())
                .andExpect(view().name("etiquettes/form"))
                .andExpect(model().attributeHasFieldErrors("etiquetteRequest", "typeMiel"));
    }

    @Test
    void generatePdf_shouldRequireDateRecolte() throws Exception {
        mockMvc.perform(post("/etiquettes/generer")
                        .param("typeMiel", "TOUTES_FLEURS")
                        .param("formatPot", "POT_500G"))
                .andExpect(status().isOk())
                .andExpect(view().name("etiquettes/form"))
                .andExpect(model().attributeHasFieldErrors("etiquetteRequest", "dateRecolte"));
    }

    @Test
    void generatePdf_shouldGenerateForForetType() throws Exception {
        // Given
        EtiquetteData mockData = EtiquetteData.builder()
                .typeMiel("Foret")
                .formatPot("1kg")
                .numeroLot("2024-FOR-001")
                .dluo(LocalDate.of(2026, 9, 1))
                .build();

        byte[] mockPdfBytes = "PDF content".getBytes();

        when(etiquetteService.buildEtiquetteData(any(EtiquetteRequest.class))).thenReturn(mockData);
        when(productService.findPriceByTypeAndFormat(any(), any())).thenReturn(new BigDecimal("15.00"));
        when(pdfService.generateEtiquetteSheet(any(EtiquetteData.class))).thenReturn(mockPdfBytes);
        when(pdfService.getLabelsPerPage()).thenReturn(21);

        // When/Then
        mockMvc.perform(post("/etiquettes/generer")
                        .param("typeMiel", "FORET")
                        .param("formatPot", "POT_1KG")
                        .param("dateRecolte", "2024-09-01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition",
                        containsString("etiquettes-foret-2024-09-01-2024-FOR-001.pdf")));

        verify(pdfService).generateEtiquetteSheet(any(EtiquetteData.class));
    }

    // ==================== GET /etiquettes/historique Tests ====================

    @Test
    void showHistorique_shouldReturnHistoriqueView() throws Exception {
        // Given
        when(etiquetteService.getRecentHistorique()).thenReturn(List.of());

        // When/Then
        mockMvc.perform(get("/etiquettes/historique"))
                .andExpect(status().isOk())
                .andExpect(view().name("etiquettes/historique"))
                .andExpect(model().attributeExists("historique"))
                .andExpect(model().attribute("activeMenu", "etiquettes"));
    }

    @Test
    void showHistorique_shouldDisplayHistoryList() throws Exception {
        // Given
        HistoriqueEtiquettes h1 = HistoriqueEtiquettes.builder()
                .id(1L)
                .typeMiel("TOUTES_FLEURS")
                .formatPot("POT_500G")
                .dateRecolte(LocalDate.of(2024, 8, 15))
                .dluo(LocalDate.of(2026, 8, 15))
                .numeroLot("2024-TF-001")
                .quantite(10)
                .dateGeneration(LocalDateTime.now())
                .prixUnitaire(new BigDecimal("8.50"))
                .build();

        when(etiquetteService.getRecentHistorique()).thenReturn(List.of(h1));

        // When/Then
        mockMvc.perform(get("/etiquettes/historique"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("historique", hasSize(1)));
    }

    // ==================== GET /etiquettes/regenerer Tests ====================

    @Test
    void regenerer_shouldPrefillForm() throws Exception {
        mockMvc.perform(get("/etiquettes/regenerer")
                        .param("type", "TOUTES_FLEURS")
                        .param("format", "POT_500G")
                        .param("date", "2024-08-15"))
                .andExpect(status().isOk())
                .andExpect(view().name("etiquettes/form"))
                .andExpect(model().attribute("etiquetteRequest",
                        hasProperty("typeMiel", equalTo(HoneyType.TOUTES_FLEURS))))
                .andExpect(model().attribute("etiquetteRequest",
                        hasProperty("formatPot", equalTo(FormatPot.POT_500G))))
                .andExpect(model().attribute("etiquetteRequest",
                        hasProperty("dateRecolte", equalTo(LocalDate.of(2024, 8, 15)))));
    }

    @Test
    void regenerer_shouldPrefillFormWithForetType() throws Exception {
        mockMvc.perform(get("/etiquettes/regenerer")
                        .param("type", "FORET")
                        .param("format", "POT_1KG")
                        .param("date", "2024-09-01"))
                .andExpect(status().isOk())
                .andExpect(view().name("etiquettes/form"))
                .andExpect(model().attribute("etiquetteRequest",
                        hasProperty("typeMiel", equalTo(HoneyType.FORET))))
                .andExpect(model().attribute("etiquetteRequest",
                        hasProperty("formatPot", equalTo(FormatPot.POT_1KG))));
    }
}
