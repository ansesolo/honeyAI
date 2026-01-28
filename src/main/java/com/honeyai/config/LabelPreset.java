package com.honeyai.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a label layout preset with dimensions and grid configuration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabelPreset {

    private String name;
    private float labelWidthMm;
    private float labelHeightMm;
    private int labelsPerRow;
    private int labelsPerColumn;
    private float marginTopMm;
    private float marginLeftMm;

    public int getLabelsPerPage() {
        return labelsPerRow * labelsPerColumn;
    }
}
