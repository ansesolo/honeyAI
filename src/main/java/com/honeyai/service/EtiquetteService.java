package com.honeyai.service;

import com.honeyai.config.EtiquetteConfig;
import com.honeyai.dto.EtiquetteData;
import com.honeyai.dto.EtiquetteRequest;
import com.honeyai.enums.HoneyType;
import com.honeyai.model.HistoriqueEtiquettes;
import com.honeyai.repository.HistoriqueEtiquettesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for label generation business logic.
 * Handles DLUO calculation.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EtiquetteService {

    private final EtiquetteConfig etiquetteConfig;
    private final HistoriqueEtiquettesRepository historiqueEtiquettesRepository;

    /**
     * Calculate DLUO (Best Before Date) by adding duration days to harvest date.
     *
     * @param dateRecolte harvest date
     * @param dureeDays   duration in days to add (default from config: 730 = 2 years)
     * @return the calculated DLUO date
     */
    @Transactional(readOnly = true)
    public LocalDate calculateDluo(LocalDate dateRecolte, Integer dureeDays) {
        if (dateRecolte == null) {
            throw new IllegalArgumentException("La date de récolte est obligatoire");
        }
        int days = (dureeDays != null) ? dureeDays : etiquetteConfig.getDluoDureeJours();
        return dateRecolte.plusDays(days);
    }

    /**
     * Calculate DLUO using default duration from configuration.
     *
     * @param dateRecolte harvest date
     * @return the calculated DLUO date
     */
    @Transactional(readOnly = true)
    public LocalDate calculateDluo(LocalDate dateRecolte) {
        return calculateDluo(dateRecolte, etiquetteConfig.getDluoDureeJours());
    }

    /**
     * Format DLUO date for display on labels.
     * French regulation requires "MM/YYYY" format (month and year only).
     *
     * @param dluo the DLUO date
     * @return formatted string "MM/YYYY"
     */
    @Transactional(readOnly = true)
    public String formatDluo(LocalDate dluo) {
        if (dluo == null) {
            return "";
        }
        return String.format("%02d/%d", dluo.getMonthValue(), dluo.getYear());
    }

    /**
     * Get the abbreviation code for a honey type.
     * TF = Toutes Fleurs, FOR = Foret, CHA = Chataignier
     *
     * @param honeyType the honey type enum
     * @return the abbreviation code
     */
    @Transactional(readOnly = true)
    public String getTypeAbbreviation(HoneyType honeyType) {
        return switch (honeyType) {
            case TOUTES_FLEURS -> "TF";
            case FORET -> "FOR";
            case CHATAIGNIER -> "CHA";
        };
    }

    /**
     * Build a complete EtiquetteData from request parameters and configuration.
     * Calculates DLUO automatically.
     *
     * @param request the label generation request
     * @return fully populated EtiquetteData ready for PDF rendering
     */
    public EtiquetteData buildEtiquetteData(EtiquetteRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La requête d'étiquette est obligatoire");
        }

        LocalDate dluo = calculateDluo(request.getDateRecolte());

        return EtiquetteData.builder()
                .typeMiel(request.getTypeMiel().getDisplayLabel())
                .formatPot(request.getFormatPot().getDisplayLabel())
                .poids("Poids net: " + request.getFormatPot().getDisplayLabel())
                .dateRecolte(formatHarvestDate(request.getDateRecolte()))
                .dluo(dluo)
                .nomApiculteur(etiquetteConfig.getNomApiculteur())
                .adresse(etiquetteConfig.getAdresse())
                .siret(etiquetteConfig.getSiret())
                .telephone(etiquetteConfig.getTelephone())
                .build();
    }

    /**
     * Format harvest date for display on labels.
     *
     * @param dateRecolte the harvest date
     * @return formatted string "Récolte: MM/YYYY"
     */
    private String formatHarvestDate(LocalDate dateRecolte) {
        if (dateRecolte == null) {
            return "";
        }
        return String.format("Récolte: %02d/%d", dateRecolte.getMonthValue(), dateRecolte.getYear());
    }

    /**
     * Save a history record after successful PDF generation.
     *
     * @param request       the original label request
     * @param data          the computed label data
     * @param prixUnitaire  the price used (may be null)
     * @param labelsPerPage number of labels on the page
     * @return the saved history record
     */
    public HistoriqueEtiquettes saveHistorique(EtiquetteRequest request, EtiquetteData data, BigDecimal prixUnitaire, int labelsPerPage) {
        HistoriqueEtiquettes historique = HistoriqueEtiquettes.builder()
                .typeMiel(request.getTypeMiel().name())
                .formatPot(request.getFormatPot().name())
                .dateRecolte(request.getDateRecolte())
                .dluo(data.getDluo())
                .quantite(labelsPerPage)
                .dateGeneration(LocalDateTime.now())
                .prixUnitaire(prixUnitaire)
                .build();

        HistoriqueEtiquettes saved = historiqueEtiquettesRepository.save(historique);
        log.info("Saved label history: type={}, quantity={}",
                saved.getTypeMiel(), saved.getQuantite());

        return saved;
    }

    /**
     * Get the most recent label generations (up to 20).
     *
     * @return list of recent history entries
     */
    @Transactional(readOnly = true)
    public List<HistoriqueEtiquettes> getRecentHistorique() {
        return historiqueEtiquettesRepository.findTop20ByOrderByDateGenerationDesc();
    }
}
