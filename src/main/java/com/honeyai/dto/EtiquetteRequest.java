package com.honeyai.dto;

import com.honeyai.enums.FormatPot;
import com.honeyai.enums.HoneyType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Request DTO for label generation.
 * Contains the parameters needed to generate honey jar labels.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtiquetteRequest {

    /**
     * Type of honey for the labels.
     */
    @NotNull(message = "Le type de miel est obligatoire")
    private HoneyType typeMiel;

    /**
     * Jar format/size.
     */
    @NotNull(message = "Le format du pot est obligatoire")
    private FormatPot formatPot;

    /**
     * Harvest date for DLUO and lot number calculation.
     */
    @NotNull(message = "La date de recolte est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateRecolte;

    /**
     * Number of labels to generate.
     */
    @NotNull(message = "La quantite est obligatoire")
    @Min(value = 1, message = "La quantite doit etre au moins 1")
    private Integer quantite;
}
