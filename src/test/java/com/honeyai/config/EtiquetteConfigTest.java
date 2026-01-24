package com.honeyai.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "honeyai.etiquettes.siret=TEST-SIRET-123",
        "honeyai.etiquettes.nom-apiculteur=Test Apiculteur",
        "honeyai.etiquettes.adresse=123 Test Address, 75001 Paris",
        "honeyai.etiquettes.telephone=01 23 45 67 89",
        "honeyai.etiquettes.dluo-duree-jours=365",
        "honeyai.etiquettes.label-width-mm=55.0",
        "honeyai.etiquettes.label-height-mm=35.0",
        "honeyai.etiquettes.labels-per-row=4",
        "honeyai.etiquettes.labels-per-column=8"
})
class EtiquetteConfigTest {

    @Autowired
    private EtiquetteConfig etiquetteConfig;

    @Test
    void shouldLoadSiret() {
        assertThat(etiquetteConfig.getSiret()).isEqualTo("TEST-SIRET-123");
    }

    @Test
    void shouldLoadNomApiculteur() {
        assertThat(etiquetteConfig.getNomApiculteur()).isEqualTo("Test Apiculteur");
    }

    @Test
    void shouldLoadAdresse() {
        assertThat(etiquetteConfig.getAdresse()).isEqualTo("123 Test Address, 75001 Paris");
    }

    @Test
    void shouldLoadTelephone() {
        assertThat(etiquetteConfig.getTelephone()).isEqualTo("01 23 45 67 89");
    }

    @Test
    void shouldLoadDluoDureeJours() {
        assertThat(etiquetteConfig.getDluoDureeJours()).isEqualTo(365);
    }

    @Test
    void shouldLoadLabelWidthMm() {
        assertThat(etiquetteConfig.getLabelWidthMm()).isEqualTo(55.0f);
    }

    @Test
    void shouldLoadLabelHeightMm() {
        assertThat(etiquetteConfig.getLabelHeightMm()).isEqualTo(35.0f);
    }

    @Test
    void shouldLoadLabelsPerRow() {
        assertThat(etiquetteConfig.getLabelsPerRow()).isEqualTo(4);
    }

    @Test
    void shouldLoadLabelsPerColumn() {
        assertThat(etiquetteConfig.getLabelsPerColumn()).isEqualTo(8);
    }

    @Test
    void shouldCalculateLabelsPerPage() {
        // 4 columns x 8 rows = 32 labels per page
        assertThat(etiquetteConfig.getLabelsPerPage()).isEqualTo(32);
    }

    @Test
    void shouldNotBeNull() {
        assertThat(etiquetteConfig).isNotNull();
    }
}
