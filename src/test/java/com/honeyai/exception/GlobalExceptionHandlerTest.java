package com.honeyai.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private Model model;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        model = new ExtendedModelMap();
    }

    @Test
    void handleNoResourceFound_shouldReturn404View_whenResourceNotFound() throws NoResourceFoundException {
        // Given
        NoResourceFoundException ex = new NoResourceFoundException(HttpMethod.GET, "/unknown/path");

        // When
        String viewName = handler.handleNoResourceFound(ex, model);

        // Then
        assertThat(viewName).isEqualTo("error/404");
        assertThat(model.getAttribute("message")).isEqualTo("La page demandée n'existe pas.");
        assertThat(model.getAttribute("status")).isEqualTo(404);
        assertThat(model.getAttribute("error")).isEqualTo("Not Found");
    }

    @Test
    void handleClientNotFound_shouldReturn404View_whenClientNotFound() {
        // Given
        ClientNotFoundException ex = new ClientNotFoundException("Client #99 not found");

        // When
        String viewName = handler.handleClientNotFound(ex, model);

        // Then
        assertThat(viewName).isEqualTo("error/404");
        assertThat(model.getAttribute("message")).isEqualTo("Client #99 not found");
        assertThat(model.getAttribute("status")).isEqualTo(404);
        assertThat(model.getAttribute("error")).isEqualTo("Not Found");
    }

    @Test
    void handlePriceNotFound_shouldReturn404View_whenPriceNotFound() {
        // Given
        PriceNotFoundException ex = new PriceNotFoundException("No price found for product #1 in year 2026");

        // When
        String viewName = handler.handlePriceNotFound(ex, model);

        // Then
        assertThat(viewName).isEqualTo("error/404");
        assertThat(model.getAttribute("message")).isEqualTo("No price found for product #1 in year 2026");
        assertThat(model.getAttribute("status")).isEqualTo(404);
    }

    @Test
    void handleInvalidStatusTransition_shouldReturnErrorView_whenInvalidTransition() {
        // Given
        InvalidStatusTransitionException ex = new InvalidStatusTransitionException("Cannot transition from PAYEE to COMMANDEE");

        // When
        String viewName = handler.handleInvalidStatusTransition(ex, model);

        // Then
        assertThat(viewName).isEqualTo("error/error");
        assertThat(model.getAttribute("message")).isEqualTo("Cannot transition from PAYEE to COMMANDEE");
        assertThat(model.getAttribute("status")).isEqualTo(409);
        assertThat(model.getAttribute("error")).isEqualTo("Conflict");
    }

    @Test
    void handlePdfGeneration_shouldReturn500View_whenPdfFails() {
        // Given
        PdfGenerationException ex = new PdfGenerationException("Font loading failed");

        // When
        String viewName = handler.handlePdfGeneration(ex, model);

        // Then
        assertThat(viewName).isEqualTo("error/500");
        assertThat(model.getAttribute("message")).isEqualTo("La génération du PDF a échoué. Veuillez réessayer.");
        assertThat(model.getAttribute("status")).isEqualTo(500);
    }

    @Test
    void handleGenericException_shouldReturn500View_whenUnexpectedError() {
        // Given
        Exception ex = new RuntimeException("Unexpected NullPointerException");

        // When
        String viewName = handler.handleGenericException(ex, model);

        // Then
        assertThat(viewName).isEqualTo("error/500");
        assertThat(model.getAttribute("message")).isEqualTo("Une erreur inattendue s'est produite. Veuillez réessayer.");
        assertThat(model.getAttribute("status")).isEqualTo(500);
        assertThat(model.getAttribute("error")).isEqualTo("Internal Server Error");
    }
}
