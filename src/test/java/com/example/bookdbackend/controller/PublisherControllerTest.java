package com.example.bookdbackend.controller;

import com.example.bookdbbackend.controller.PublisherController;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Publisher;
import com.example.bookdbbackend.service.IPublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PublisherControllerTest {

    @Mock
    private IPublisherService iPublisherService;

    @InjectMocks
    private PublisherController publisherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPublishers() {
        when(iPublisherService.getAllPublishers()).thenReturn(Collections.singletonList(new Publisher()));

        ResponseEntity<List<Publisher>> response = publisherController.getAllPublishers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iPublisherService, times(1)).getAllPublishers();
    }

    @Test
    void testGetBooksByPublisherName() {
        String name = "Test Publisher";
        when(iPublisherService.getBooksByPublisherName(name)).thenReturn(Collections.singletonList(new Book()));

        ResponseEntity<List<Book>> response = publisherController.getBooksByPublisherName(name);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iPublisherService, times(1)).getBooksByPublisherName(name);
    }

    @Test
    void testGetBooksByPublisherCountry() {
        String country = "Test Country";
        when(iPublisherService.getBooksByPublisherCountry(country)).thenReturn(Collections.singletonList(new Book()));

        ResponseEntity<List<Book>> response = publisherController.getBooksByPublisherCountry(country);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iPublisherService, times(1)).getBooksByPublisherCountry(country);
    }

    @Test
    void testGetPublisherById() {
        Long id = 1L;
        Publisher publisher = new Publisher();
        when(iPublisherService.getPublisherById(id)).thenReturn(publisher);

        ResponseEntity<Publisher> response = publisherController.getPublisherById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iPublisherService, times(1)).getPublisherById(id);
    }

    @Test
    void testAddPublisher() {
        Publisher publisher = new Publisher();
        when(iPublisherService.addPublisher(publisher)).thenReturn(publisher);

        Publisher response = publisherController.addPublisher(publisher);

        assertEquals(publisher, response);
        verify(iPublisherService, times(1)).addPublisher(publisher);
    }

    @Test
    void testUpdatePublisher() {
        Long id = 1L;
        Publisher publisher = new Publisher();
        Publisher existingPublisher = new Publisher();
        when(iPublisherService.getPublisherById(id)).thenReturn(existingPublisher);
        when(iPublisherService.updatePublisher(any(Publisher.class), eq(id))).thenReturn(publisher);

        ResponseEntity<Publisher> response = publisherController.updatePublisher(publisher, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iPublisherService, times(1)).getPublisherById(id);
        verify(iPublisherService, times(1)).updatePublisher(any(Publisher.class), eq(id));
    }
}