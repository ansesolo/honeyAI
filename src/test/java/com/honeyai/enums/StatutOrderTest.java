package com.honeyai.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StatutOrderTest {

    @Test
    void getDisplayLabel_shouldReturnFrenchLabels() {
        assertThat(OrderStatus.ORDERED.getDisplayLabel()).isEqualTo("Commandée");
        assertThat(OrderStatus.RECOVERED.getDisplayLabel()).isEqualTo("Récupérée");
        assertThat(OrderStatus.PAID.getDisplayLabel()).isEqualTo("Payée");
    }

    @Test
    void values_shouldContainThreeStatuses() {
        assertThat(OrderStatus.values()).hasSize(3);
    }

    @Test
    void valueOf_shouldReturnCorrectEnum() {
        assertThat(OrderStatus.valueOf("ORDERED")).isEqualTo(OrderStatus.ORDERED);
        assertThat(OrderStatus.valueOf("RECOVERED")).isEqualTo(OrderStatus.RECOVERED);
        assertThat(OrderStatus.valueOf("PAID")).isEqualTo(OrderStatus.PAID);
    }
}
