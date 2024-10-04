package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorRequest {
    private String firstName;
    private String lastName;
}
