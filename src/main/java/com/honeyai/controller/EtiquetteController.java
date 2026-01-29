package com.honeyai.controller;

import com.honeyai.config.LabelPreset;
import com.honeyai.config.LabelPresetsConfig;
import com.honeyai.dto.EtiquetteData;
import com.honeyai.dto.EtiquetteRequest;
import com.honeyai.enums.FormatPot;
import com.honeyai.enums.HoneyType;
import com.honeyai.service.EtiquetteService;
import com.honeyai.service.PdfService;
import com.honeyai.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for label generation UI and PDF download.
 */
@Controller
@RequestMapping("/etiquettes")
@RequiredArgsConstructor
@Slf4j
public class EtiquetteController {

    private final EtiquetteService etiquetteService;
    private final PdfService pdfService;
    private final ProductService productService;
    private final LabelPresetsConfig labelPresetsConfig;

    /**
     * Display the label generation form.
     */
    @GetMapping
    public String showForm(Model model) {
        EtiquetteRequest request = EtiquetteRequest.builder()
                .typeMiel(HoneyType.TOUTES_FLEURS)
                .formatPot(FormatPot.POT_500G)
                .dateRecolte(LocalDate.now())
                .presetName(labelPresetsConfig.getDefault().getName())
                .build();

        model.addAttribute("etiquetteRequest", request);
        model.addAttribute("honeyTypes", HoneyType.values());
        model.addAttribute("formatPots", FormatPot.values());
        model.addAttribute("labelPresets", labelPresetsConfig.getPresets());
        model.addAttribute("etiquetteConfig", pdfService.getEtiquetteConfig());
        model.addAttribute("activeMenu", "etiquettes");

        return "etiquettes/form";
    }

    /**
     * Generate PDF labels and return as downloadable file.
     */
    @PostMapping("/generer")
    public Object generatePdf(@Valid @ModelAttribute("etiquetteRequest") EtiquetteRequest request,
                              BindingResult bindingResult,
                              Model model) {

        // Validation errors - return to form
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors for label generation: {}", bindingResult.getAllErrors());
            model.addAttribute("honeyTypes", HoneyType.values());
            model.addAttribute("formatPots", FormatPot.values());
            model.addAttribute("labelPresets", labelPresetsConfig.getPresets());
            model.addAttribute("etiquetteConfig", pdfService.getEtiquetteConfig());
            model.addAttribute("activeMenu", "etiquettes");
            return "etiquettes/form";
        }

        try {
            log.info("Generating labels: type={}, format={}, date={}, preset={}",
                    request.getTypeMiel(), request.getFormatPot(),
                    request.getDateRecolte(), request.getPresetName());

            // Resolve preset
            LabelPreset preset = labelPresetsConfig.findByName(request.getPresetName())
                    .orElse(labelPresetsConfig.getDefault());

            // Build label data
            EtiquetteData data = etiquetteService.buildEtiquetteData(request);

            // Lookup price
            BigDecimal price = productService.findPriceByTypeAndFormat(
                    request.getTypeMiel(), request.getFormatPot());
            data.setPrixUnitaire(price);

            if (price == null) {
                log.warn("No price found for {} {}, generating labels without price",
                        request.getTypeMiel(), request.getFormatPot());
            }

            // Generate PDF (one full page) with preset
            byte[] pdfBytes = pdfService.generateEtiquetteSheet(data, preset);

            // Save history record
            int labelsPerPage = preset.getLabelsPerPage();
            etiquetteService.saveHistorique(request, data, price, labelsPerPage);

            // Build filename
            String filename = buildFilename(request);

            log.info("PDF generated successfully: {} bytes, filename={}",
                    pdfBytes.length, filename);

            // Return PDF as download
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);

        } catch (Exception e) {
            log.error("Error generating PDF labels: {}", e.getMessage(), e);

            model.addAttribute("error", "Erreur lors de la generation du PDF. Veuillez reessayer.");
            model.addAttribute("honeyTypes", HoneyType.values());
            model.addAttribute("formatPots", FormatPot.values());
            model.addAttribute("labelPresets", labelPresetsConfig.getPresets());
            model.addAttribute("etiquetteConfig", pdfService.getEtiquetteConfig());
            model.addAttribute("activeMenu", "etiquettes");
            return "etiquettes/form";
        }
    }

    /**
     * Build the PDF filename.
     * Format: etiquettes-{type}-{date}.pdf
     */
    private String buildFilename(EtiquetteRequest request) {
        String typeName = request.getTypeMiel().name().toLowerCase().replace("_", "-");
        String dateStr = request.getDateRecolte().format(DateTimeFormatter.ISO_LOCAL_DATE);

        return String.format("etiquettes-%s-%s.pdf", typeName, dateStr);
    }

    /**
     * Display label generation history.
     */
    @GetMapping("/historique")
    public String showHistorique(Model model) {
        model.addAttribute("historique", etiquetteService.getRecentHistorique());
        model.addAttribute("activeMenu", "etiquettes");
        return "etiquettes/historique";
    }

    /**
     * Pre-fill form for re-generation from history.
     */
    @GetMapping("/regenerer")
    public String regenerer(
            @org.springframework.web.bind.annotation.RequestParam("type") String typeMiel,
            @org.springframework.web.bind.annotation.RequestParam("format") String formatPot,
            @org.springframework.web.bind.annotation.RequestParam("date") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate dateRecolte,
            Model model) {

        EtiquetteRequest request = EtiquetteRequest.builder()
                .typeMiel(HoneyType.valueOf(typeMiel))
                .formatPot(FormatPot.valueOf(formatPot))
                .dateRecolte(dateRecolte)
                .presetName(labelPresetsConfig.getDefault().getName())
                .build();

        model.addAttribute("etiquetteRequest", request);
        model.addAttribute("honeyTypes", HoneyType.values());
        model.addAttribute("formatPots", FormatPot.values());
        model.addAttribute("labelPresets", labelPresetsConfig.getPresets());
        model.addAttribute("etiquetteConfig", pdfService.getEtiquetteConfig());
        model.addAttribute("activeMenu", "etiquettes");

        return "etiquettes/form";
    }
}
