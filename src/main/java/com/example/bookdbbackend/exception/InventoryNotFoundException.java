package com.example.bookdbbackend.exception;

/**
 * Exception thrown when an inventory item is not found.
 */
public class InventoryNotFoundException extends RuntimeException {

    /**
     * Constructs a new InventoryNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public InventoryNotFoundException(String message) {
        super(message);
    }
}