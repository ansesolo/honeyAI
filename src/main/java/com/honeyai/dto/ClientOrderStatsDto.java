package com.honeyai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for client order statistics displayed on client detail page.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientOrderStatsDto {

    private long totalOrders;
    private BigDecimal totalPaidAmount;
    private LocalDate lastOrderDate;

    public boolean hasOrders() {
        return totalOrders > 0;
    }
}
