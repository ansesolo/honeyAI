package com.honeyai.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoResourceFound(NoResourceFoundException ex, Model model) {
        log.warn("Resource not found: {}", ex.getMessage());
        model.addAttribute("message", "La page demandée n'existe pas.");
        model.addAttribute("status", 404);
        model.addAttribute("error", "Not Found");
        return "error/404";
    }

    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleClientNotFound(ClientNotFoundException ex, Model model) {
        log.warn("Client not found: {}", ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", 404);
        model.addAttribute("error", "Not Found");
        return "error/404";
    }

    @ExceptionHandler(PriceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePriceNotFound(PriceNotFoundException ex, Model model) {
        log.warn("Price not found: {}", ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", 404);
        model.addAttribute("error", "Not Found");
        return "error/404";
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleInvalidStatusTransition(InvalidStatusTransitionException ex, Model model) {
        log.warn("Invalid status transition: {}", ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", 409);
        model.addAttribute("error", "Conflict");
        return "error/error";
    }

    @ExceptionHandler(PdfGenerationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handlePdfGeneration(PdfGenerationException ex, Model model) {
        log.error("PDF generation failed: {}", ex.getMessage(), ex);
        model.addAttribute("message", "La génération du PDF a échoué. Veuillez réessayer.");
        model.addAttribute("status", 500);
        model.addAttribute("error", "Internal Server Error");
        return "error/500";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        model.addAttribute("message", "Une erreur inattendue s'est produite. Veuillez réessayer.");
        model.addAttribute("status", 500);
        model.addAttribute("error", "Internal Server Error");
        return "error/500";
    }
}
