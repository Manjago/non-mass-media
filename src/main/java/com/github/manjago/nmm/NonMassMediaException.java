package com.github.manjago.nmm;

public class NonMassMediaException extends RuntimeException {
    public NonMassMediaException(String message) {
        super(message);
    }

    public NonMassMediaException(String message, Throwable cause) {
        super(message, cause);
    }
}
