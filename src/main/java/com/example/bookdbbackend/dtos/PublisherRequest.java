package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherRequest {
    private long publisher_id;
    private String country;
    private String name;
}
