package com.example.bookdbbackend.exception;

/**
 * Exception thrown when an order already exists.
 */
public class OrderAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new OrderAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public OrderAlreadyExistsException(String message) {
        super(message);
    }
}