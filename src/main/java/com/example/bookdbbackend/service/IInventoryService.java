package com.example.bookdbbackend.service;

import com.example.bookdbbackend.model.Inventory;

import java.util.Map;

public interface IInventoryService {
    Inventory updateInventory(Map<String, Object> updates, Long id);
    Inventory getInventory(Long id);

}
