package com.honeyai.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LabelPresetsConfigTest {

    private EtiquetteConfig etiquetteConfig;

    @BeforeEach
    void setUp() {
        etiquetteConfig = new EtiquetteConfig();
        etiquetteConfig.setSiret("12345678901234");
        etiquetteConfig.setNomApiculteur("Test Apiculteur");
        etiquetteConfig.setAdresse("123 Test Street");
        etiquetteConfig.setLabelWidthMm(60.0f);
        etiquetteConfig.setLabelHeightMm(40.0f);
        etiquetteConfig.setLabelsPerRow(3);
        etiquetteConfig.setLabelsPerColumn(7);
        etiquetteConfig.setMarginTopMm(10.0f);
        etiquetteConfig.setMarginLeftMm(10.0f);
    }

    @Test
    void init_shouldLoadPresetsFromYamlFile() {
        // The data/label-presets.yml file exists in the project
        LabelPresetsConfig config = new LabelPresetsConfig(etiquetteConfig);
        config.init();

        assertThat(config.getPresets()).isNotEmpty();
        assertThat(config.getPresets().get(0).getName()).contains("Standard");
    }

    @Test
    void getDefault_shouldReturnFirstPreset() {
        LabelPresetsConfig config = new LabelPresetsConfig(etiquetteConfig);
        config.init();

        LabelPreset defaultPreset = config.getDefault();
        assertThat(defaultPreset).isNotNull();
        assertThat(defaultPreset.getName()).isNotBlank();
    }

    @Test
    void findByName_shouldReturnMatchingPreset() {
        LabelPresetsConfig config = new LabelPresetsConfig(etiquetteConfig);
        config.init();

        String firstName = config.getPresets().get(0).getName();
        Optional<LabelPreset> found = config.findByName(firstName);

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(firstName);
    }

    @Test
    void findByName_shouldReturnEmptyForUnknownName() {
        LabelPresetsConfig config = new LabelPresetsConfig(etiquetteConfig);
        config.init();

        Optional<LabelPreset> found = config.findByName("Nonexistent Preset");
        assertThat(found).isEmpty();
    }

    @Test
    void findByName_shouldReturnEmptyForNull() {
        LabelPresetsConfig config = new LabelPresetsConfig(etiquetteConfig);
        config.init();

        assertThat(config.findByName(null)).isEmpty();
        assertThat(config.findByName("")).isEmpty();
        assertThat(config.findByName("  ")).isEmpty();
    }

    @Test
    void getLabelsPerPage_shouldComputeCorrectly() {
        LabelPresetsConfig config = new LabelPresetsConfig(etiquetteConfig);
        config.init();

        LabelPreset preset = config.getPresets().get(0);
        assertThat(preset.getLabelsPerPage())
                .isEqualTo(preset.getLabelsPerRow() * preset.getLabelsPerColumn());
    }
}
