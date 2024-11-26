package com.example.bookdbbackend.service;

import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Publisher;

import java.util.List;

/**
 * Service interface for handling publisher-related operations.
 */
public interface IPublisherService {

    /**
     * Adds a new publisher.
     *
     * @param publisher the publisher to add
     * @return the added publisher
     */
    Publisher addPublisher(Publisher publisher);

    /**
     * Retrieves all publishers.
     *
     * @return a list of all publishers
     */
    List<Publisher> getAllPublishers();

    /**
     * Retrieves a publisher by its ID.
     *
     * @param id the ID of the publisher
     * @return the publisher with the specified ID
     */
    Publisher getPublisherById(Long id);

    /**
     * Retrieves books by the publisher's country.
     *
     * @param country the country of the publisher
     * @return a list of books by publishers from the specified country
     */
    List<Book> getBooksByPublisherCountry(String country);

    /**
     * Retrieves books by the publisher's name.
     *
     * @param name the name of the publisher
     * @return a list of books by the specified publisher
     */
    List<Book> getBooksByPublisherName(String name);

    /**
     * Updates a publisher with the specified updates.
     *
     * @param publisher the publisher with updates
     * @param id the ID of the publisher to update
     * @return the updated publisher
     */
    Publisher updatePublisher(Publisher publisher, Long id);

    /**
     * Deletes a publisher by its ID.
     *
     * @param id the ID of the publisher to delete
     */
    void deletePublisher(Long id);
}