package com.honeyai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for product with current year price.
 * Used in order form to display products with their prices in dropdown.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPriceDto {

    private Long id;
    private String name;
    private String unit;
    private BigDecimal price;

    /**
     * Get display label for dropdown: "Product Name - 12,50 EUR"
     */
    public String getDisplayLabel() {
        if (price != null) {
            return String.format("%s - %s EUR", name,
                    price.toString().replace('.', ','));
        }
        return name + " - Prix non defini";
    }
}
