package com.honeyai.enums;

/**
 * Types of honey available for products.
 */
public enum HoneyType {
    TOUTES_FLEURS("Toutes Fleurs"),
    FORET("Forêt"),
    CHATAIGNIER("Châtaignier");

    private final String displayLabel;

    HoneyType(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }
}
