package com.example.bookdbbackend.exception;

/**
 * Exception thrown when invalid data is encountered.
 */
public class InvalidDataException extends RuntimeException {

    /**
     * Constructs a new InvalidDataException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidDataException(String message) {
        super(message);
    }
}