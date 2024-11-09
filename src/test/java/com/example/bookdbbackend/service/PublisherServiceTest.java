package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.BookAlreadyExistsException;
import com.example.bookdbbackend.exception.BookNotFoundException;
import com.example.bookdbbackend.exception.PublisherNotFoundException;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Publisher;
import com.example.bookdbbackend.repository.PublisherRepository;
import com.example.bookdbbackend.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private PublisherService publisherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPublisher() {
        Publisher publisher = new Publisher();
        publisher.setPublisher_id(1L);

        when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);

        Publisher result = publisherService.addPublisher(publisher);

        assertNotNull(result);
        assertEquals(1L, result.getPublisher_id());
        verify(publisherRepository, times(1)).save(publisher);
    }

    @Test
    void testAddPublisherAlreadyExists() {
        Publisher publisher = new Publisher();
        publisher.setPublisher_id(1L);

        when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(publisher));

        assertThrows(BookAlreadyExistsException.class, () -> publisherService.addPublisher(publisher));
    }

    @Test
    void testGetAllPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        publishers.add(new Publisher());

        when(publisherRepository.findAll()).thenReturn(publishers);

        List<Publisher> result = publisherService.getAllPublishers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(publisherRepository, times(1)).findAll();
    }

    @Test
    void testGetPublisherById() {

    }

    @Test
    void testGetPublisherByIdNotFound() {
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> publisherService.getPublisherById(1L));
    }

    @Test
    void testUpdatePublisher() {
        Publisher publisher = new Publisher();
        publisher.setPublisher_id(1L);

        when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);

        Publisher result = publisherService.updatePublisher(publisher, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getPublisher_id());
        verify(publisherRepository, times(1)).findById(1L);
        verify(publisherRepository, times(1)).save(publisher);
    }

    @Test
    void testUpdatePublisherNotFound() {
        Publisher publisher = new Publisher();
        publisher.setPublisher_id(1L);

        when(publisherRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> publisherService.updatePublisher(publisher, 1L));
    }

    @Test
    void testDeletePublisher() {
        Publisher publisher = new Publisher();
        publisher.setPublisher_id(1L);

        when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(publisher));

        publisherService.deletePublisher(1L);

        verify(publisherRepository, times(1)).findById(1L);
        verify(publisherRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePublisherNotFound() {
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> publisherService.deletePublisher(1L));
    }
}