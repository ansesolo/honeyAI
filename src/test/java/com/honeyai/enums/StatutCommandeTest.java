package com.honeyai.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StatutCommandeTest {

    @Test
    void getDisplayLabel_shouldReturnFrenchLabels() {
        assertThat(StatutCommande.COMMANDEE.getDisplayLabel()).isEqualTo("Commandée");
        assertThat(StatutCommande.RECUPEREE.getDisplayLabel()).isEqualTo("Récupérée");
        assertThat(StatutCommande.PAYEE.getDisplayLabel()).isEqualTo("Payée");
    }

    @Test
    void values_shouldContainThreeStatuses() {
        assertThat(StatutCommande.values()).hasSize(3);
    }

    @Test
    void valueOf_shouldReturnCorrectEnum() {
        assertThat(StatutCommande.valueOf("COMMANDEE")).isEqualTo(StatutCommande.COMMANDEE);
        assertThat(StatutCommande.valueOf("RECUPEREE")).isEqualTo(StatutCommande.RECUPEREE);
        assertThat(StatutCommande.valueOf("PAYEE")).isEqualTo(StatutCommande.PAYEE);
    }
}
