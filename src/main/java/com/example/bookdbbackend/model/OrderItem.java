package com.example.bookdbbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entity class representing an item in an order.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_items")
@IdClass(OrderItem.OrderItemId.class)
public class OrderItem {

    /**
     * The order associated with this item.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    /**
     * The book associated with this item.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    /**
     * The quantity of the book in this order item.
     */
    private int quantity;

    /**
     * The price of the book in this order item.
     */
    private BigDecimal price;

    /**
     * Composite key class for OrderItem.
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemId implements Serializable {
        private Long order;
        private Long book;
    }
}