package com.honeyai.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for label generation.
 * Contains exploitation information and label layout settings.
 */
@ConfigurationProperties(prefix = "honeyai.etiquettes")
@Validated
@Data
public class EtiquetteConfig {

    /**
     * SIRET number of the beekeeping exploitation.
     */
    @NotBlank(message = "Le SIRET est obligatoire")
    private String siret;

    /**
     * Name of the beekeeper or exploitation.
     */
    @NotBlank(message = "Le nom de l'apiculteur est obligatoire")
    private String nomApiculteur;

    /**
     * Full address of the exploitation.
     */
    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    /**
     * Contact phone number.
     */
    private String telephone;

    /**
     * DLUO duration in days (default: 730 = 2 years).
     */
    @NotNull
    @Min(1)
    private Integer dluoDureeJours = 730;

    /**
     * Label width in millimeters.
     */
    @NotNull
    @Positive
    private Float labelWidthMm = 60.0f;

    /**
     * Label height in millimeters.
     */
    @NotNull
    @Positive
    private Float labelHeightMm = 40.0f;

    /**
     * Number of labels per row on A4 sheet.
     */
    @NotNull
    @Min(1)
    private Integer labelsPerRow = 3;

    /**
     * Number of labels per column on A4 sheet.
     */
    @NotNull
    @Min(1)
    private Integer labelsPerColumn = 7;

    /**
     * Top margin in millimeters.
     */
    @NotNull
    @Min(0)
    private Float marginTopMm = 10.0f;

    /**
     * Left margin in millimeters.
     */
    @NotNull
    @Min(0)
    private Float marginLeftMm = 10.0f;

    /**
     * Total labels per A4 page.
     */
    public int getLabelsPerPage() {
        return labelsPerRow * labelsPerColumn;
    }
}
