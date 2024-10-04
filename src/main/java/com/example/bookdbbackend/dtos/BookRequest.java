package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class BookRequest {
    private String title;
    private String isbn;
    private String genre;
    private String type;
    private int publicationYear;
    private BigDecimal price;
    private String bookCondition;
    private boolean reserved;
    private String imageUrl;

    // Either add this if creating a new publisher
    private PublisherRequest publisher;

    // Or this if referencing an existing publisher
    private Long publisherId;

    // Authors information
    private List<AuthorRequest> authors;

    // Inventory information
    private InventoryRequest inventory;

}
