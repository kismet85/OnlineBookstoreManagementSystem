package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Publisher;
import com.example.bookdbbackend.service.IBookService;
import com.example.bookdbbackend.service.IPublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PublisherController {
    private final IPublisherService iPublisherService;

    private Publisher mergeNewAndOldPublisherForUpdate(Publisher existingPublisher, Publisher newPublisher) {
        // If the new book object has a value for a field, update the existing book object with that value
        existingPublisher.setCountry(newPublisher.getCountry() != null ? newPublisher.getCountry() : existingPublisher.getCountry());
        return existingPublisher;
    }

    @GetMapping
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        return new ResponseEntity<>(iPublisherService.getAllPublishers(), HttpStatus.OK);
    }

    @GetMapping("/{name}/books")
    public ResponseEntity<List<Book>> getBooksByPublisherName(@PathVariable String name) {
        List<Book> books = iPublisherService.getBooksByPublisherName(name);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/country/{country}/books")
    public ResponseEntity<List<Book>> getBooksByPublisherCountry(@PathVariable String country) {
        List<Book> books = iPublisherService.getBooksByPublisherCountry(country);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @GetMapping("/publishers/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable Long id) {
        try {
            Publisher publisher = iPublisherService.getPublisherById(id);
            return new ResponseEntity<>(publisher, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   @GetMapping("/publishers/{name}")
    public ResponseEntity<List<Book>> getBooksByPublisherId(@PathVariable String name) {
        List<Book> publisherBooks = iPublisherService.getBooksByPublisherName(name);
        return new ResponseEntity<>(publisherBooks, HttpStatus.OK);
    }

    @PostMapping
    public Publisher addPublisher(@RequestBody Publisher publisher) {
        return iPublisherService.addPublisher(publisher);
    }

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
