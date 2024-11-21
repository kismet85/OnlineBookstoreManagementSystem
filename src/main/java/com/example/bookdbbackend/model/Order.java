package com.example.bookdbbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Entity class representing an order.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    /**
     * The ID of the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;

    /**
     * The total amount of the order.
     */
    @Column(name = "total", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal total;

    /**
     * The user who placed the order.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The date the order was placed.
     */
    @Column(name = "order_date", columnDefinition = "DATE")
    private LocalDate orderDate;

    /**
     * The list of items in the order.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> orderItems;
}