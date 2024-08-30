package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookdbbackend.model.Publisher;

import java.util.Optional;

public interface PublisherInventory extends JpaRepository<Publisher, Long>  {
    Optional<Publisher> findPublisherByCountry(String country);
}
