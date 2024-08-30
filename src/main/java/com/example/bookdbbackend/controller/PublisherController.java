package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Publisher;
import com.example.bookdbbackend.service.IBookService;
import com.example.bookdbbackend.service.IPublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController {
    private final IPublisherService iPublisherService;

    @GetMapping
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        return new ResponseEntity<>(iPublisherService.getAllPublishers(), HttpStatus.FOUND);
    }

    @GetMapping("/publishers/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable Long id) {
        try {
            Publisher publisher = iPublisherService.getPublisherById(id);
            return new ResponseEntity<>(publisher, HttpStatus.FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   /* @GetMapping("/publishers/{publisher_id}")
    public ResponseEntity<List<Publisher>> getBooksByPublisherId(@PathVariable Long publisher_id) {
        List<Publisher> publisher = iPublisherService.getAllPublishers(publisher_id);
        return new ResponseEntity<>(publisher, HttpStatus.FOUND);
    }*/

    @PostMapping
    public Publisher addPublisher(@RequestBody Publisher publisher) {
        return iPublisherService.addPublisher(publisher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@RequestBody Publisher publisher, @PathVariable Long id) {
        try {
            Publisher updatePublisher = iPublisherService.updatePublisher(publisher, id);
            return new ResponseEntity<>(updatePublisher, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
