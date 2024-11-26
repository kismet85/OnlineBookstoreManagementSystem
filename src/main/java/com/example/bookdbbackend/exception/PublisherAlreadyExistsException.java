package com.example.bookdbbackend.exception;

/**
 * Exception thrown when a publisher already exists.
 */
public class PublisherAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new PublisherAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public PublisherAlreadyExistsException(String message) {
        super(message);
    }
}