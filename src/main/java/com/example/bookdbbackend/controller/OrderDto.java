package com.example.bookdbbackend.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto {
    private List<OrderItemDto> orderItems;
}