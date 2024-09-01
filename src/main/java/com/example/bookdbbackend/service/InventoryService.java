package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.InventoryNotFoundException;
import com.example.bookdbbackend.model.Inventory;
import com.example.bookdbbackend.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService{

    @Autowired
    private final InventoryRepository inventoryRepository;
    @Override
    public Inventory updateInventory(Inventory inventory, Long id) {
        if (!inventoryRepository.findById(id).isPresent()){
            throw new InventoryNotFoundException("Inventory with id " + inventory.getInventory_id() + " not found");
        }
        inventory.setInventory_id(id);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory getInventory(Long id) {
        if (!inventoryRepository.findById(id).isPresent()){
            throw new InventoryNotFoundException("Inventory with id " + id + " not found");
        }
        return inventoryRepository.findById(id).get();
    }
}
