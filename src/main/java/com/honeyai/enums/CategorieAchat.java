package com.honeyai.enums;

public enum CategorieAchat {
    CIRE("Cire"),
    POTS("Pots"),
    COUVERCLES("Couvercles"),
    NOURRISSEMENT("Nourrissement"),
    AUTRE("Autre");

    private final String displayLabel;

    CategorieAchat(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }
}
