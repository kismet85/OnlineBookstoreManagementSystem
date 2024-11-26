package com.example.bookdbbackend.exception;

/**
 * Exception thrown when a book already exists.
 */
public class BookAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new BookAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}