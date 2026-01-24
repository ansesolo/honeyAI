package com.honeyai.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FormatPotTest {

    @Test
    void pot500g_shouldHaveCorrectDisplayLabel() {
        assertThat(FormatPot.POT_500G.getDisplayLabel()).isEqualTo("500g");
    }

    @Test
    void pot500g_shouldHaveCorrectWeight() {
        assertThat(FormatPot.POT_500G.getWeightKg()).isEqualTo(0.5);
    }

    @Test
    void pot1kg_shouldHaveCorrectDisplayLabel() {
        assertThat(FormatPot.POT_1KG.getDisplayLabel()).isEqualTo("1kg");
    }

    @Test
    void pot1kg_shouldHaveCorrectWeight() {
        assertThat(FormatPot.POT_1KG.getWeightKg()).isEqualTo(1.0);
    }

    @Test
    void shouldHaveTwoFormats() {
        assertThat(FormatPot.values()).hasSize(2);
    }
}
