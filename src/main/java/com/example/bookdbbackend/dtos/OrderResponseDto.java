package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {
    private BigDecimal total;
    private LocalDate orderDate;
    private String userEmail;
    private List<OrderItemResponseDto> orderItems;


}
