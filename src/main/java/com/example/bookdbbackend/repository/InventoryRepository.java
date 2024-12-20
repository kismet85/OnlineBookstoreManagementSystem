package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Inventory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


/**
 * Repository interface for handling Inventory-related database operations.
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Updates the inventory with the specified stock levels and reserved stock.
     *
     * @param stockLevelUsed the used stock level
     * @param stockLevelNew the new stock level
     * @param reservedStock the reserved stock
     * @param inventoryId the ID of the inventory to update
     * @return the number of rows affected
     */
    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.stock_level_used = ?1, i.stock_level_new = ?2, i.reserved_stock = ?3 WHERE i.inventory_id = ?4")
    int updateInventory(int stockLevelUsed, int stockLevelNew, int reservedStock, Long inventoryId);
}