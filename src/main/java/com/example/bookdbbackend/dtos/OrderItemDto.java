package com.example.bookdbbackend.dtos;

import com.example.bookdbbackend.model.Book;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Data Transfer Object for order item requests.
 */
@Getter
@Setter
public class OrderItemDto {
    /**
     * The ID of the book.
     */
    private Long book_id;

    /**
     * The quantity of the book ordered.
     */
    private int quantity;

    /**
     * The price of the book.
     */
    private BigDecimal price;
}