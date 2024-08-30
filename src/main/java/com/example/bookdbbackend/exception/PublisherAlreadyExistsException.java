package com.example.bookdbbackend.exception;

public class PublisherAlreadyExistsException extends RuntimeException{

    public PublisherAlreadyExistsException(String message) {
        super(message);
    }
}
