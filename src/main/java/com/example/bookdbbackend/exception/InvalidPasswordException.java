package com.example.bookdbbackend.exception;

/**
 * Exception thrown when an invalid password is encountered.
 */
public class InvalidPasswordException extends RuntimeException {

    /**
     * Constructs a new InvalidPasswordException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidPasswordException(String message) {
        super(message);
    }
}