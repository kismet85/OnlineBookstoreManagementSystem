package com.example.bookdbbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bookdbbackend.model.Publisher;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for handling Publisher-related database operations.
 */
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    /**
     * Finds publishers by name.
     *
     * @param name the name of the publisher
     * @return a list of publishers with the specified name
     */
    List<Publisher> findPublisherByName(String name);

    /**
     * Finds publishers by country.
     *
     * @param country the country of the publisher
     * @return a list of publishers from the specified country
     */
    List<Publisher> findPublisherByCountry(String country);
}