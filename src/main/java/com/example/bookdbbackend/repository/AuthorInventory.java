package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorInventory extends JpaRepository<Inventory, Long> {

}