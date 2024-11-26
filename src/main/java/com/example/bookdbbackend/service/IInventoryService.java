package com.example.bookdbbackend.service;

import com.example.bookdbbackend.model.Inventory;

import java.util.Map;

/**
 * Service interface for handling inventory-related operations.
 */
public interface IInventoryService {

    /**
     * Updates an inventory with the specified updates.
     *
     * @param updates the updates to apply
     * @param id the ID of the inventory to update
     * @return the updated inventory
     */
    Inventory updateInventory(Map<String, Object> updates, Long id);

    /**
     * Retrieves an inventory by its ID.
     *
     * @param id the ID of the inventory
     * @return the inventory with the specified ID
     */
    Inventory getInventory(Long id);
}