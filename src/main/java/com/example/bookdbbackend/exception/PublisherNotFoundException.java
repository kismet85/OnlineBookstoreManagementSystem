package com.example.bookdbbackend.exception;

public class PublisherNotFoundException extends RuntimeException{
    public PublisherNotFoundException(String message) {
        super(message);
    }
}
