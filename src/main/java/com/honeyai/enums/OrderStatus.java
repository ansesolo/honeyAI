package com.honeyai.enums;

/**
 * Order status workflow: COMMANDEE -> RECUPEREE -> PAYEE
 */
public enum OrderStatus {
    ORDERED("Commandée"),
    RECOVERED("Récupérée"),
    PAID("Payée");

    private final String displayLabel;

    OrderStatus(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }
}
