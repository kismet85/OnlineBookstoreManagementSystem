package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.InventoryNotFoundException;
import com.example.bookdbbackend.model.Inventory;
import com.example.bookdbbackend.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Set up the test inventory
        inventory = new Inventory();
        inventory.setInventory_id(1L);
        inventory.setStock_level_new(10);
        inventory.setStock_level_used(5);
    }

    @Test
    void testUpdateInventory_Success() {
        Map<String, Object> updateData = Map.of(
                "stock_level_new", 20,
                "stock_level_used", 15
        );

        // Mock the repository to return the inventory
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(inventory)).thenReturn(inventory);

        // Perform the update
        Inventory updatedInventory = inventoryService.updateInventory(updateData, 1L);

        // Verify that the values were updated correctly
        assertNotNull(updatedInventory);
        assertEquals(20, updatedInventory.getStock_level_new());
        assertEquals(15, updatedInventory.getStock_level_used());

        // Verify interactions with repository
        verify(inventoryRepository, times(1)).findById(1L);
        verify(inventoryRepository, times(1)).save(inventory);
    }

    @Test
    void testUpdateInventory_NotFound() {
        Map<String, Object> updateData = Map.of(
                "stock_level_new", 20,
                "stock_level_used", 15
        );

        // Mock the repository to return empty (inventory not found)
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform the update and expect InventoryNotFoundException
        assertThrows(InventoryNotFoundException.class, () -> inventoryService.updateInventory(updateData, 1L));

        // Verify interactions with repository
        verify(inventoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetInventory_Success() {
        // Mock the repository to return the inventory
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));

        // Perform the get inventory
        Inventory foundInventory = inventoryService.getInventory(1L);

        // Verify the inventory was returned
        assertNotNull(foundInventory);
        assertEquals(1L, foundInventory.getInventory_id());
        assertEquals(10, foundInventory.getStock_level_new());
        assertEquals(5, foundInventory.getStock_level_used());

        // Verify interactions with repository, expecting it to be called twice
        verify(inventoryRepository, times(2)).findById(1L);
    }


    @Test
    void testGetInventory_NotFound() {
        // Mock the repository to return empty (inventory not found)
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform the get inventory and expect InventoryNotFoundException
        assertThrows(InventoryNotFoundException.class, () -> inventoryService.getInventory(1L));

        // Verify interactions with repository
        verify(inventoryRepository, times(1)).findById(1L);
    }
}
