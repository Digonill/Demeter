package com.server.demeter.services.exception;

public class ObjectNotEnabledException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ObjectNotEnabledException(final String message) {
        super(message);
    }

}