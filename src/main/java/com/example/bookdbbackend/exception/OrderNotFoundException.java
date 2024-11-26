package com.example.bookdbbackend.exception;

/**
 * Exception thrown when an order is not found.
 */
public class OrderNotFoundException extends RuntimeException {

    /**
     * Constructs a new OrderNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}