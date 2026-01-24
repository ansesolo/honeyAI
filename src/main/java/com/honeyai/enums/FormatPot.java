package com.honeyai.enums;

/**
 * Jar format/size options for honey products.
 */
public enum FormatPot {
    POT_500G("500g", 0.5),
    POT_1KG("1kg", 1.0);

    private final String displayLabel;
    private final double weightKg;

    FormatPot(String displayLabel, double weightKg) {
        this.displayLabel = displayLabel;
        this.weightKg = weightKg;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public double getWeightKg() {
        return weightKg;
    }
}
