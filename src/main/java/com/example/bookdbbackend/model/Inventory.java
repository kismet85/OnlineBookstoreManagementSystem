package com.example.bookdbbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing an inventory for each book.
 */
@Entity
@Table(name = "inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    /**
     * The ID of the inventory.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventory_id;

    /**
     * The stock level of used books.
     */
    private int stock_level_used;

    /**
     * The stock level of new books.
     */
    private int stock_level_new;

    /**
     * The reserved stock level.
     */
    private int reserved_stock;
}