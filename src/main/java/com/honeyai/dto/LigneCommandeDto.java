package com.honeyai.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for order line form binding.
 * Used to capture product line data from the order creation form.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneCommandeDto {

    @NotNull(message = "Le produit est obligatoire")
    private Long productId;

    @NotNull(message = "La quantite est obligatoire")
    @Min(value = 1, message = "La quantite doit etre au moins 1")
    private Integer quantite;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @Positive(message = "Le prix unitaire doit etre positif")
    private BigDecimal prixUnitaire;
}
