package com.example.bookdbbackend.service;

import com.example.bookdbbackend.model.Author;
import java.util.List;

/**
 * Service interface for handling author-related operations.
 */
public interface IAuthorService {

    /**
     * Retrieves all authors.
     *
     * @return a list of all authors
     */
    List<Author> getAllAuthors();

    /**
     * Retrieves an author by their ID.
     *
     * @param id the ID of the author
     * @return the author with the specified ID
     */
    Author getAuthorById(Long id);
}