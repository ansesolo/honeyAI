package com.honeyai.exception;

public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(String message) {
        super(message);
    }

    public PriceNotFoundException(Long productId, Integer year) {
        super(String.format("Price not found for product #%d and year %d", productId, year));
    }
}
