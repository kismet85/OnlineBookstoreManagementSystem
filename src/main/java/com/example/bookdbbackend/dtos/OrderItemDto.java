package com.example.bookdbbackend.dtos;

import com.example.bookdbbackend.model.Book;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemDto {
    private Long book_id;
    private int quantity;
    private BigDecimal price;
}