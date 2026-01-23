package com.honeyai.enums;

/**
 * Order status workflow: COMMANDEE -> RECUPEREE -> PAYEE
 */
public enum StatutCommande {
    COMMANDEE("Commandée"),
    RECUPEREE("Récupérée"),
    PAYEE("Payée");

    private final String displayLabel;

    StatutCommande(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }
}
