package com.example.bookdbbackend.exception;

public class OrderAlreadyExistsException extends RuntimeException {
    public OrderAlreadyExistsException(String message) {
        super(message); }
}
