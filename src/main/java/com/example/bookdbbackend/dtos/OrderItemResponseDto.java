package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for order item response.
 */
@Getter
@Setter
public class OrderItemResponseDto {
    /**
     * The title of the book.
     */
    private String bookTitle;
}