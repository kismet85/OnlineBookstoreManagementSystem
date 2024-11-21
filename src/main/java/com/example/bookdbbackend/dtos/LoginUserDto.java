package com.example.bookdbbackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for login user requests.
 */
@Getter
@Setter
public class LoginUserDto {
    /**
     * The email of the user.
     * Must be a valid email format.
     */
    @NotEmpty(message = "Email is required")
    @Email(message = "Email is invalid", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    /**
     * The password of the user.
     * Must not be empty.
     */
    @NotEmpty(message = "Password is required")
    private String password;
}