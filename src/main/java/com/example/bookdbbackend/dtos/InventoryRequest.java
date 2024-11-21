package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for inventory requests.
 */
@Getter
@Setter
public class InventoryRequest {
    /**
     * The stock level of used books.
     */
    private int stockLevelUsed;

    /**
     * The stock level of new books.
     */
    private int stockLevelNew;

    /**
     * The reserved stock level.
     */
    private int reservedStock;
}