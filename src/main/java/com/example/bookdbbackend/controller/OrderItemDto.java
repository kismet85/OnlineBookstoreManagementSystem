package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.model.Book;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemDto {
    private Book book;
    private int quantity;
    private BigDecimal price;
}