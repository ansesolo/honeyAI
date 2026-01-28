package com.honeyai.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Loads label presets from data/label-presets.yml.
 * Falls back to EtiquetteConfig defaults if the file is absent.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LabelPresetsConfig {

    private final EtiquetteConfig etiquetteConfig;

    private List<LabelPreset> presets = new ArrayList<>();

    @PostConstruct
    @SuppressWarnings("unchecked")
    void init() {
        Path yamlPath = Path.of("data/label-presets.yml");
        if (!Files.exists(yamlPath)) {
            log.warn("Label presets file not found at {}, using EtiquetteConfig defaults", yamlPath);
            presets.add(buildFallbackPreset());
            return;
        }

        try (InputStream is = Files.newInputStream(yamlPath)) {
            Yaml yaml = new Yaml();
            Map<String, Object> root = yaml.load(is);
            List<Map<String, Object>> presetList = (List<Map<String, Object>>) root.get("presets");

            if (presetList == null || presetList.isEmpty()) {
                log.warn("No presets found in {}, using EtiquetteConfig defaults", yamlPath);
                presets.add(buildFallbackPreset());
                return;
            }

            for (Map<String, Object> entry : presetList) {
                presets.add(LabelPreset.builder()
                        .name((String) entry.get("name"))
                        .labelWidthMm(toFloat(entry.get("labelWidthMm")))
                        .labelHeightMm(toFloat(entry.get("labelHeightMm")))
                        .labelsPerRow(toInt(entry.get("labelsPerRow")))
                        .labelsPerColumn(toInt(entry.get("labelsPerColumn")))
                        .marginTopMm(toFloat(entry.get("marginTopMm")))
                        .marginLeftMm(toFloat(entry.get("marginLeftMm")))
                        .build());
            }

            log.info("Loaded {} label presets from {}", presets.size(), yamlPath);

        } catch (Exception e) {
            log.error("Failed to load label presets from {}: {}", yamlPath, e.getMessage());
            presets.add(buildFallbackPreset());
        }
    }

    public List<LabelPreset> getPresets() {
        return presets;
    }

    public LabelPreset getDefault() {
        return presets.isEmpty() ? buildFallbackPreset() : presets.get(0);
    }

    public Optional<LabelPreset> findByName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        return presets.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();
    }

    private LabelPreset buildFallbackPreset() {
        return LabelPreset.builder()
                .name("Standard")
                .labelWidthMm(etiquetteConfig.getLabelWidthMm())
                .labelHeightMm(etiquetteConfig.getLabelHeightMm())
                .labelsPerRow(etiquetteConfig.getLabelsPerRow())
                .labelsPerColumn(etiquetteConfig.getLabelsPerColumn())
                .marginTopMm(etiquetteConfig.getMarginTopMm())
                .marginLeftMm(etiquetteConfig.getMarginLeftMm())
                .build();
    }

    private float toFloat(Object value) {
        if (value instanceof Number n) {
            return n.floatValue();
        }
        return Float.parseFloat(String.valueOf(value));
    }

    private int toInt(Object value) {
        if (value instanceof Number n) {
            return n.intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }
}
