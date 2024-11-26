package com.example.bookdbbackend.exception;

/**
 * Exception thrown when a publisher is not found.
 */
public class PublisherNotFoundException extends RuntimeException {

    /**
     * Constructs a new PublisherNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public PublisherNotFoundException(String message) {
        super(message);
    }
}