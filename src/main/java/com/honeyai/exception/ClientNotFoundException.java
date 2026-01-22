package com.honeyai.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(Long id) {
        super("Ce client n'existe pas ou a ete supprime");
    }
}
