package com.translation.translationservicemgmt.exception;

public class TranslationNotFoundException extends RuntimeException {

    public TranslationNotFoundException(Long id) {
        super("Translation not found with id: " + id);
    }

    public TranslationNotFoundException(String message) {
        super(message);
    }

    public TranslationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
