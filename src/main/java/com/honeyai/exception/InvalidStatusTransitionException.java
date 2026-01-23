package com.honeyai.exception;

import com.honeyai.enums.StatutCommande;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(String message) {
        super(message);
    }

    public InvalidStatusTransitionException(StatutCommande from, StatutCommande to) {
        super(String.format("Invalid status transition from %s to %s", from, to));
    }
}
