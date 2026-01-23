package com.honeyai.exception;

import com.honeyai.enums.OrderStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(String message) {
        super(message);
    }

    public InvalidStatusTransitionException(OrderStatus from, OrderStatus to) {
        super(String.format("Invalid status transition from %s to %s", from, to));
    }
}
