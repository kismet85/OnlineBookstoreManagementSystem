package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.BookAlreadyExistsException;
import com.example.bookdbbackend.exception.BookNotFoundException;
import com.example.bookdbbackend.exception.PublisherNotFoundException;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Publisher;
import com.example.bookdbbackend.repository.PublisherRepository;
import com.example.bookdbbackend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling publisher-related operations.
 */
@Service
@RequiredArgsConstructor
public class PublisherService implements IPublisherService {

    @Autowired
    private final PublisherRepository publisherRepository;

    @Autowired
    private final BookRepository bookRepository;

    /**
     * Adds a new publisher.
     *
     * @param publisher the publisher to add
     * @return the added publisher
     * @throws BookAlreadyExistsException if the publisher already exists
     */
    @Override
    public Publisher addPublisher(Publisher publisher) {
        if (publisherAlreadyExists(publisher.getPublisher_id())) {
            throw new BookAlreadyExistsException("Publisher with id " + publisher.getPublisher_id() + " already exists");
        }
        return publisherRepository.save(publisher);
    }

    /**
     * Checks if a publisher already exists by its ID.
     *
     * @param id the ID of the publisher
     * @return true if the publisher exists, false otherwise
     */
    private boolean publisherAlreadyExists(Long id) {
        return publisherRepository.findById(id).isPresent();
    }

    /**
     * Retrieves all publishers.
     *
     * @return a list of all publishers
     */
    @Override
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    /**
     * Retrieves a publisher by its ID.
     *
     * @param id the ID of the publisher
     * @return the publisher with the specified ID
     * @throws PublisherNotFoundException if the publisher is not found
     */
    @Override
    public Publisher getPublisherById(Long id) {
        if (!publisherRepository.findById(id).isPresent()) {
            throw new PublisherNotFoundException("Publisher not found with id: " + id);
        }
        return publisherRepository.findById(id).get();
    }

    /**
     * Retrieves books by the publisher's name.
     *
     * @param name the name of the publisher
     * @return a list of books by the specified publisher
     * @throws PublisherNotFoundException if the publisher is not found
     */
    @Override
    public List<Book> getBooksByPublisherName(String name) {
        List<Publisher> publishers = publisherRepository.findPublisherByName(name);
        if (publishers.isEmpty()) {
            throw new PublisherNotFoundException("Publisher not found with name: " + name);
        }

        List<Book> books = new ArrayList<>();
        for (Publisher publisher : publishers) {
            books.addAll(bookRepository.findBooksByPublisherId(publisher.getPublisher_id()));
        }

        return books;
    }

    /**
     * Retrieves books by the publisher's country.
     *
     * @param country the country of the publisher
     * @return a list of books by publishers from the specified country
     * @throws PublisherNotFoundException if the publisher is not found
     */
    @Override
    public List<Book> getBooksByPublisherCountry(String country) {
        List<Publisher> publishers = publisherRepository.findPublisherByCountry(country);
        if (publishers.isEmpty()) {
            throw new PublisherNotFoundException("Publisher not found from country: " + country);
        }

        List<Book> books = new ArrayList<>();
        for (Publisher publisher : publishers) {
            books.addAll(bookRepository.findBooksByPublisherId(publisher.getPublisher_id()));
        }

        return books;
    }

    /**
     * Updates a publisher with the specified updates.
     *
     * @param publisher the publisher with updates
     * @param id the ID of the publisher to update
     * @return the updated publisher
     * @throws BookNotFoundException if the publisher is not found
     */
    @Override
    public Publisher updatePublisher(Publisher publisher, Long id) {
        if (!publisherAlreadyExists(id)) {
            throw new BookNotFoundException("Publisher with id " + id + " not found");
        }
        publisher.setPublisher_id(id);
        return publisherRepository.save(publisher);
    }

    /**
     * Deletes a publisher by its ID.
     *
     * @param id the ID of the publisher to delete
     * @throws BookNotFoundException if the publisher is not found
     */
    @Override
    public void deletePublisher(Long id) {
        if (!publisherAlreadyExists(id)) {
            throw new BookNotFoundException("Publisher with id " + id + " not found");
        }
        publisherRepository.deleteById(id);
    }
}