package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.InventoryNotFoundException;
import com.example.bookdbbackend.model.Inventory;
import com.example.bookdbbackend.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service class for handling inventory-related operations.
 */
@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {

    @Autowired
    private final InventoryRepository inventoryRepository;

    /**
     * Updates an inventory with the specified updates.
     *
     * @param updates the updates to apply
     * @param id the ID of the inventory to update
     * @return the updated inventory
     * @throws InventoryNotFoundException if the inventory is not found
     */
    @Override
    public Inventory updateInventory(Map<String, Object> updates, Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElse(null);
        if (inventory == null) {
            throw new InventoryNotFoundException("Inventory not found");
        }
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            if ("stock_level_new".equals(entry.getKey())) {
                inventory.setStock_level_new((Integer) entry.getValue());
            } else if ("stock_level_used".equals(entry.getKey())) {
                inventory.setStock_level_used((Integer) entry.getValue());
            }
        }
        return inventoryRepository.save(inventory);
    }

    /**
     * Retrieves an inventory by its ID.
     *
     * @param id the ID of the inventory
     * @return the inventory with the specified ID
     * @throws InventoryNotFoundException if the inventory is not found
     */
    @Override
    public Inventory getInventory(Long id) {
        if (!inventoryRepository.findById(id).isPresent()) {
            throw new InventoryNotFoundException("Inventory with id " + id + " not found");
        }
        return inventoryRepository.findById(id).get();
    }
}