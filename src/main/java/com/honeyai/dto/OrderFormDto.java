package com.honeyai.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for order creation form binding.
 * Captures all form data before converting to Commande entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFormDto {

    @NotNull(message = "Le client est obligatoire")
    private Long clientId;

    @NotNull(message = "La date de commande est obligatoire")
    private LocalDate orderDate;

    private String notes;

    @Valid
    @Size(min = 1, message = "La commande doit contenir au moins une ligne")
    @Builder.Default
    private List<OrderLineDto> lines = new ArrayList<>();

    /**
     * Add a new empty ligne for form initialization.
     */
    public void addEmptyLine() {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.add(new OrderLineDto());
    }
}
