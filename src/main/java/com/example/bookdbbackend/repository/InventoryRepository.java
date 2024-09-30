package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Inventory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.stock_level_used = ?1, i.stock_level_new = ?2, i.reserved_stock = ?3 WHERE i.inventory_id = ?4")
    int updateInventory(int stockLevelUsed, int stockLevelNew, int reservedStock, Long inventoryId);


}