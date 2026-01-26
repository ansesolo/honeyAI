package com.honeyai.controller;

import com.honeyai.config.EtiquetteConfig;
import com.honeyai.dto.EtiquetteData;
import com.honeyai.dto.EtiquetteRequest;
import com.honeyai.enums.FormatPot;
import com.honeyai.enums.HoneyType;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
    void showForm_shouldHaveDefaultQuantity() throws Exception {
        mockMvc.perform(get("/etiquettes"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("etiquetteRequest",
                        hasProperty("quantite", equalTo(10))));
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

    // ==================== POST /etiquettes/generate Tests ====================

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
        when(pdfService.generateEtiquetteSheet(any(EtiquetteData.class), anyInt())).thenReturn(mockPdfBytes);

        // When/Then
        mockMvc.perform(post("/etiquettes/generate")
                        .param("typeMiel", "TOUTES_FLEURS")
                        .param("formatPot", "POT_500G")
                        .param("dateRecolte", "2024-08-15")
                        .param("quantite", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition",
                        containsString("attachment; filename=")))
                .andExpect(header().string("Content-Disposition",
                        containsString("etiquettes-toutes-fleurs-2024-08-15-2024-TF-001.pdf")));

        verify(etiquetteService).buildEtiquetteData(any(EtiquetteRequest.class));
        verify(pdfService).generateEtiquetteSheet(any(EtiquetteData.class), eq(10));
    }

    @Test
    void generatePdf_shouldReturnFormWithErrors_whenValidationFails() throws Exception {
        mockMvc.perform(post("/etiquettes/generate")
                        .param("typeMiel", "TOUTES_FLEURS")
                        .param("formatPot", "POT_500G")
                        .param("dateRecolte", "2024-08-15")
                        .param("quantite", "0")) // Invalid: must be >= 1
                .andExpect(status().isOk())
                .andExpect(view().name("etiquettes/form"))
                .andExpect(model().attributeHasFieldErrors("etiquetteRequest", "quantite"));

        verify(etiquetteService, never()).buildEtiquetteData(any());
    }

    @Test
    void generatePdf_shouldReturnFormWithError_whenServiceFails() throws Exception {
        // Given
        when(etiquetteService.buildEtiquetteData(any(EtiquetteRequest.class)))
                .thenThrow(new RuntimeException("Service error"));

        // When/Then
        mockMvc.perform(post("/etiquettes/generate")
                        .param("typeMiel", "TOUTES_FLEURS")
                        .param("formatPot", "POT_500G")
                        .param("dateRecolte", "2024-08-15")
                        .param("quantite", "10"))
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
        when(pdfService.generateEtiquetteSheet(any(EtiquetteData.class), anyInt())).thenReturn(mockPdfBytes);

        // When/Then
        mockMvc.perform(post("/etiquettes/generate")
                        .param("typeMiel", "TOUTES_FLEURS")
                        .param("formatPot", "POT_500G")
                        .param("dateRecolte", "2024-08-15")
                        .param("quantite", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    void generatePdf_shouldRequireTypeMiel() throws Exception {
        mockMvc.perform(post("/etiquettes/generate")
                        .param("formatPot", "POT_500G")
                        .param("dateRecolte", "2024-08-15")
                        .param("quantite", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("etiquettes/form"))
                .andExpect(model().attributeHasFieldErrors("etiquetteRequest", "typeMiel"));
    }

    @Test
    void generatePdf_shouldRequireDateRecolte() throws Exception {
        mockMvc.perform(post("/etiquettes/generate")
                        .param("typeMiel", "TOUTES_FLEURS")
                        .param("formatPot", "POT_500G")
                        .param("quantite", "10"))
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
        when(pdfService.generateEtiquetteSheet(any(EtiquetteData.class), anyInt())).thenReturn(mockPdfBytes);

        // When/Then
        mockMvc.perform(post("/etiquettes/generate")
                        .param("typeMiel", "FORET")
                        .param("formatPot", "POT_1KG")
                        .param("dateRecolte", "2024-09-01")
                        .param("quantite", "25"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition",
                        containsString("etiquettes-foret-2024-09-01-2024-FOR-001.pdf")));

        verify(pdfService).generateEtiquetteSheet(any(EtiquetteData.class), eq(25));
    }
}
