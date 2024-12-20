package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for handling OrderItem-related database operations.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}