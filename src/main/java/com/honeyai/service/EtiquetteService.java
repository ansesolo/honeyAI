package com.honeyai.service;

import com.honeyai.config.EtiquetteConfig;
import com.honeyai.dto.EtiquetteData;
import com.honeyai.dto.EtiquetteRequest;
import com.honeyai.enums.HoneyType;
import com.honeyai.model.HistoriqueEtiquettes;
import com.honeyai.model.LotsEtiquettes;
import com.honeyai.repository.HistoriqueEtiquettesRepository;
import com.honeyai.repository.LotsEtiquettesRepository;
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
 * Handles DLUO calculation and lot number generation.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EtiquetteService {

    private final EtiquetteConfig etiquetteConfig;
    private final LotsEtiquettesRepository lotsEtiquettesRepository;
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
     * Generate a unique lot number for a given honey type and harvest date.
     * Format: YYYY-TYPE-NNN where:
     * - YYYY = harvest year
     * - TYPE = honey type abbreviation (TF, FOR, CHA)
     * - NNN = sequential number per year/type combination (001, 002, etc.)
     *
     * @param honeyType   the type of honey
     * @param dateRecolte the harvest date
     * @return the generated lot number (e.g., "2024-TF-001")
     */
    public String generateNumeroLot(HoneyType honeyType, LocalDate dateRecolte) {
        if (honeyType == null) {
            throw new IllegalArgumentException("Le type de miel est obligatoire");
        }
        if (dateRecolte == null) {
            throw new IllegalArgumentException("La date de récolte est obligatoire");
        }

        int annee = dateRecolte.getYear();
        String typeAbbreviation = getTypeAbbreviation(honeyType);

        // Fetch or create lot counter for this year/type combination
        LotsEtiquettes lot = lotsEtiquettesRepository
            .findByAnneeAndTypeMiel(annee, typeAbbreviation)
            .orElseGet(() -> LotsEtiquettes.builder()
                .annee(annee)
                .typeMiel(typeAbbreviation)
                .dernierNumero(0)
                .build());

        // Increment and save
        lot.setDernierNumero(lot.getDernierNumero() + 1);
        lotsEtiquettesRepository.save(lot);

        log.info("Generated lot number: {}-{}-{} (sequence #{})",
            annee, typeAbbreviation,
            String.format("%03d", lot.getDernierNumero()),
            lot.getDernierNumero());

        return String.format("%d-%s-%03d", annee, typeAbbreviation, lot.getDernierNumero());
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
     * Get the current sequence number for a year/type combination without incrementing.
     *
     * @param honeyType   the type of honey
     * @param annee       the year
     * @return the current sequence number (0 if no lots generated yet)
     */
    @Transactional(readOnly = true)
    public int getCurrentSequenceNumber(HoneyType honeyType, int annee) {
        String typeAbbreviation = getTypeAbbreviation(honeyType);
        return lotsEtiquettesRepository
            .findByAnneeAndTypeMiel(annee, typeAbbreviation)
            .map(LotsEtiquettes::getDernierNumero)
            .orElse(0);
    }

    /**
     * Build a complete EtiquetteData from request parameters and configuration.
     * Calculates DLUO and generates lot number automatically.
     *
     * @param request the label generation request
     * @return fully populated EtiquetteData ready for PDF rendering
     */
    public EtiquetteData buildEtiquetteData(EtiquetteRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La requête d'étiquette est obligatoire");
        }

        LocalDate dluo = calculateDluo(request.getDateRecolte());
        String numeroLot = generateNumeroLot(request.getTypeMiel(), request.getDateRecolte());

        return EtiquetteData.builder()
                .typeMiel(request.getTypeMiel().getDisplayLabel())
                .formatPot(request.getFormatPot().getDisplayLabel())
                .poids("Poids net: " + request.getFormatPot().getDisplayLabel())
                .dateRecolte(formatHarvestDate(request.getDateRecolte()))
                .dluo(dluo)
                .numeroLot(numeroLot)
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
     * @return the saved history record
     */
    public HistoriqueEtiquettes saveHistorique(EtiquetteRequest request, EtiquetteData data, BigDecimal prixUnitaire) {
        HistoriqueEtiquettes historique = HistoriqueEtiquettes.builder()
                .typeMiel(request.getTypeMiel().name())
                .formatPot(request.getFormatPot().name())
                .dateRecolte(request.getDateRecolte())
                .dluo(data.getDluo())
                .numeroLot(data.getNumeroLot())
                .quantite(request.getQuantite())
                .dateGeneration(LocalDateTime.now())
                .prixUnitaire(prixUnitaire)
                .build();

        HistoriqueEtiquettes saved = historiqueEtiquettesRepository.save(historique);
        log.info("Saved label history: lot={}, type={}, quantity={}",
                saved.getNumeroLot(), saved.getTypeMiel(), saved.getQuantite());

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
