package com.example.bookdbbackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for user registration requests.
 */
@Getter
@Setter
public class RegisterUserDto {

    /**
     * The email of the user.
     */
    @NotEmpty(message = "Email is required")
    @Email(message = "Email is invalid", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    /**
     * The password of the user.
     */
    @NotEmpty(message = "Password is required")
    private String password;

    /**
     * The first name of the user.
     */
    @NotEmpty(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String first_name;

    /**
     * The last name of the user.
     */
    @NotEmpty(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String last_name;
}