package com.bazlur.api.exception;

/**
 * @author Bazlur Rahman Rokon
 * @since 1/30/16.
 */
public class TodoNotFoundException extends Exception {
    public TodoNotFoundException(String message) {
        super(message);
    }
}
