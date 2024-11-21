package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for order response.
 */
@Getter
@Setter
public class OrderResponseDto {
    /**
     * The total amount of the order.
     */
    private BigDecimal total;

    /**
     * The date the order was placed.
     */
    private LocalDate orderDate;

    /**
     * The email of the user who placed the order.
     */
    private String userEmail;

    /**
     * The list of items in the order.
     */
    private List<OrderItemResponseDto> orderItems;
}