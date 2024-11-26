package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for handling Order-related database operations.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds orders by the user's ID.
     *
     * @param user_id the ID of the user
     * @return a list of orders associated with the specified user ID
     */
    @Query("SELECT o FROM Order o JOIN o.user u WHERE u.user_id = :user_id")
    List<Order> findOrdersByUserId(Long user_id);
}