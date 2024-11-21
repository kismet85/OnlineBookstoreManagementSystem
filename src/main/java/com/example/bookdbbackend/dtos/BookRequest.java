package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object for book requests.
 */
@Getter
@Setter
public class BookRequest {
    /**
     * The title of the book.
     */
    private String title;

    /**
     * The ISBN of the book.
     */
    private String isbn;

    /**
     * The genre of the book.
     */
    private String genre;

    /**
     * The type of the book.
     */
    private String type;

    /**
     * The publication year of the book.
     */
    private int publicationYear;

    /**
     * The price of the book.
     */
    private BigDecimal price;

    /**
     * The condition of the book.
     */
    private String bookCondition;

    /**
     * Whether the book is reserved.
     */
    private boolean reserved;

    /**
     * The URL of the book's image.
     */
    private String imageUrl;

    /**
     * The publisher information if creating a new publisher.
     */
    private PublisherRequest publisher;

    /**
     * The ID of the publisher if referencing an existing publisher.
     */
    private Long publisherId;

    /**
     * The list of authors of the book.
     */
    private List<AuthorRequest> authors;

    /**
     * The inventory information of the book.
     */
    private InventoryRequest inventory;
}