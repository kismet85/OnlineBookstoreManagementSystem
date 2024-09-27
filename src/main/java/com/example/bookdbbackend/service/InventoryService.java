package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.InventoryNotFoundException;
import com.example.bookdbbackend.model.Inventory;
import com.example.bookdbbackend.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {

    @Autowired
    private final InventoryRepository inventoryRepository;

    @Override
    public Inventory updateInventory(Map<String, Object> updates, Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElse(null);
        if (inventory == null) {
            throw new InventoryNotFoundException("Inventory not found");
        }
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            switch (entry.getKey()) {
                case "stock_level_new":
                    inventory.setStock_level_new((Integer) entry.getValue());
                    break;
                case "stock_level_used":
                    inventory.setStock_level_used((Integer) entry.getValue());
                    break;
            }
        }
        return inventoryRepository.save(inventory);
    }
    @Override
    public Inventory getInventory(Long id) {
        if (!inventoryRepository.findById(id).isPresent()) {
            throw new InventoryNotFoundException("Inventory with id " + id + " not found");
        }
        return inventoryRepository.findById(id).get();
    }
}


