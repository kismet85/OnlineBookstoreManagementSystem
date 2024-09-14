package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long user_id;
    private List<OrderItemDto> orderItems;
}