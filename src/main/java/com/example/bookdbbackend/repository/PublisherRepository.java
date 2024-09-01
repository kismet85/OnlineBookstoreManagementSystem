package com.example.bookdbbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookdbbackend.model.Publisher;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long>  {
    Optional<Publisher> findPublisherByCountry(String country);
}
