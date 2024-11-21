package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for publisher requests.
 */
@Getter
@Setter
public class PublisherRequest {
    /**
     * The ID of the publisher.
     */
    private long publisher_id;

    /**
     * The country of the publisher.
     */
    private String country;

    /**
     * The name of the publisher.
     */
    private String name;
}