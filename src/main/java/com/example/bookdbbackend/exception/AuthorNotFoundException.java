package com.example.bookdbbackend.exception;

/**
 * Exception thrown when an author is not found.
 */
public class AuthorNotFoundException extends RuntimeException {

    /**
     * Constructs a new AuthorNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public AuthorNotFoundException(String message) {
        super(message);
    }
}