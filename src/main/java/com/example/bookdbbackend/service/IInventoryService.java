package com.example.bookdbbackend.service;

import com.example.bookdbbackend.model.Inventory;

public interface IInventoryService {
    Inventory updateInventory(Inventory inventory, Long id);
    Inventory getInventory(Long id);

}
