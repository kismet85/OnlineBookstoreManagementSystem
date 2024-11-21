package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for author requests.
 */
@Getter
@Setter
public class AuthorRequest {
    /**
     * The first name of the author.
     */
    private String firstName;

    /**
     * The last name of the author.
     */
    private String lastName;
}