package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object for order requests.
 */
@Getter
@Setter
public class OrderDto {
    /**
     * The ID of the user placing the order.
     */
    private Long user_id;

    /**
     * The list of items in the order.
     */
    private List<OrderItemDto> orderItems;
}