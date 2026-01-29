package com.honeyai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data model containing all computed fields for rendering a single label.
 * This is the fully resolved data ready for PDF generation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtiquetteData {

    /**
     * Honey type display label (e.g., "Miel Toutes Fleurs").
     */
    private String typeMiel;

    /**
     * Jar format display label (e.g., "500g").
     */
    private String formatPot;

    /**
     * Formatted harvest date (e.g., "Recolte: 08/2025").
     */
    private String dateRecolte;

    /**
     * Computed DLUO date (harvest date + dluo duration).
     */
    private LocalDate dluo;

    /**
     * Beekeeper/exploitation name.
     */
    private String nomApiculteur;

    /**
     * Full address of the exploitation.
     */
    private String adresse;

    /**
     * SIRET number.
     */
    private String siret;

    /**
     * Contact phone number.
     */
    private String telephone;

    /**
     * Unit price from current year tarif.
     */
    private BigDecimal prixUnitaire;

    /**
     * Weight display (e.g., "Poids net: 500g").
     */
    private String poids;

    /**
     * DLUO formatted for display (e.g., "A consommer de preference avant: 08/2027").
     */
    public String getDluoFormatted() {
        if (dluo == null) {
            return "";
        }
        return String.format("%02d/%d", dluo.getMonthValue(), dluo.getYear());
    }
}
