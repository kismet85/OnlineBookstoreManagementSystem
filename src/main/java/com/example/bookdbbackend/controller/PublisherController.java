package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Publisher;
import com.example.bookdbbackend.service.IPublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling publisher-related requests.
 */
@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PublisherController {
    private final IPublisherService iPublisherService;

    /**
     * Merges the new publisher data with the existing publisher data for update.
     *
     * @param existingPublisher the existing publisher
     * @param newPublisher the new publisher data
     * @return the merged publisher
     */
    private Publisher mergeNewAndOldPublisherForUpdate(Publisher existingPublisher, Publisher newPublisher) {
        existingPublisher.setCountry(newPublisher.getCountry() != null ? newPublisher.getCountry() : existingPublisher.getCountry());
        return existingPublisher;
    }

    /**
     * Endpoint for getting all publishers.
     *
     * @return a list of all publishers
     */
    @GetMapping
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        return new ResponseEntity<>(iPublisherService.getAllPublishers(), HttpStatus.OK);
    }

    /**
     * Endpoint for getting books by publisher name.
     *
     * @param name the name of the publisher
     * @return a list of books by the specified publisher
     */
    @GetMapping("/{name}/books")
    public ResponseEntity<List<Book>> getBooksByPublisherName(@PathVariable String name) {
        List<Book> books = iPublisherService.getBooksByPublisherName(name);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Endpoint for getting books by publisher country.
     *
     * @param country the country of the publisher
     * @return a list of books by publishers from the specified country
     */
    @GetMapping("/country/{country}/books")
    public ResponseEntity<List<Book>> getBooksByPublisherCountry(@PathVariable String country) {
        List<Book> books = iPublisherService.getBooksByPublisherCountry(country);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Endpoint for getting a publisher by ID.
     *
     * @param id the ID of the publisher
     * @return the publisher with the specified ID
     */
    @GetMapping("/publishers/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable Long id) {
        try {
            Publisher publisher = iPublisherService.getPublisherById(id);
            return new ResponseEntity<>(publisher, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for getting books by publisher name.
     *
     * @param name the name of the publisher
     * @return a list of books by the specified publisher
     */
    @GetMapping("/publishers/{name}")
    public ResponseEntity<List<Book>> getBooksByPublisherId(@PathVariable String name) {
        List<Book> publisherBooks = iPublisherService.getBooksByPublisherName(name);
        return new ResponseEntity<>(publisherBooks, HttpStatus.OK);
    }

    /**
     * Endpoint for adding a new publisher.
     *
     * @param publisher the publisher to add
     * @return the added publisher
     */
    @PostMapping
    public Publisher addPublisher(@RequestBody Publisher publisher) {
        return iPublisherService.addPublisher(publisher);
    }

    /**
     * Endpoint for updating a publisher by ID.
     *
     * @param publisher the updated publisher data
     * @param id the ID of the publisher to update
     * @return the updated publisher
     */
    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@RequestBody Publisher publisher, @PathVariable Long id) {
        try {
            Publisher existingPublisher = iPublisherService.getPublisherById(id);
            Publisher mergedPublisher = mergeNewAndOldPublisherForUpdate(existingPublisher, publisher);
            Publisher updatedPublisher = iPublisherService.updatePublisher(mergedPublisher, id);

            return new ResponseEntity<>(updatedPublisher, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}