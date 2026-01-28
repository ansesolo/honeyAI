package com.honeyai.enums;

/**
 * Jar format/size options for honey products.
 */
public enum FormatPot {
    POT_500G("500g", "pot 500g", 0.5),
    POT_1KG("1kg", "pot 1kg", 1.0);

    private final String displayLabel;
    private final String unitLabel;
    private final double weightKg;

    FormatPot(String displayLabel, String unitLabel, double weightKg) {
        this.displayLabel = displayLabel;
        this.unitLabel = unitLabel;
        this.weightKg = weightKg;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public String getUnitLabel() {
        return unitLabel;
    }

    public double getWeightKg() {
        return weightKg;
    }
}
