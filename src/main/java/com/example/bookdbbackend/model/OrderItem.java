package com.example.bookdbbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_items")
@IdClass(OrderItem.OrderItemId.class)
public class OrderItem {

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private int quantity;
    private BigDecimal price;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemId implements Serializable {
        private Long order;
        private Long book;
    }

}