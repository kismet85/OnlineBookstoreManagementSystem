package com.example.bookdbbackend.responses;

import lombok.Getter;
import lombok.Setter;

/**
 * Response class for login operations.
 */
@Getter
@Setter
public class LoginResponse {

    /**
     * The JWT token.
     */
    private String token;

    /**
     * The expiration time of the token in milliseconds.
     */
    private long expiresIn;
}