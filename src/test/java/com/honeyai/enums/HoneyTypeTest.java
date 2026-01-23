package com.honeyai.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HoneyTypeTest {

    @Test
    void getDisplayLabel_shouldReturnFrenchLabels() {
        assertThat(HoneyType.TOUTES_FLEURS.getDisplayLabel()).isEqualTo("Toutes Fleurs");
        assertThat(HoneyType.FORET.getDisplayLabel()).isEqualTo("Forêt");
        assertThat(HoneyType.CHATAIGNIER.getDisplayLabel()).isEqualTo("Châtaignier");
    }

    @Test
    void values_shouldContainThreeTypes() {
        assertThat(HoneyType.values()).hasSize(3);
    }
}
