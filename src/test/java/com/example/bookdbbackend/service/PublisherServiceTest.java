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
import java.util.Collections;
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
        Publisher publisher = new Publisher();
        publisher.setPublisher_id(1L);

        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));

        Publisher result = publisherService.getPublisherById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getPublisher_id());
        verify(publisherRepository, times(2)).findById(1L);
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
    @Test
    void testGetBooksByPublisherName() {
        Publisher publisher = new Publisher();
        publisher.setPublisher_id(1L);
        publisher.setName("Test Publisher");

        Book book1 = new Book();
        book1.setBook_id(1L);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setBook_id(2L);
        book2.setTitle("Book 2");

        when(publisherRepository.findPublisherByName("Test Publisher")).thenReturn(List.of(publisher));
        when(bookRepository.findBooksByPublisherId(1L)).thenReturn(List.of(book1, book2));

        List<Book> books = publisherService.getBooksByPublisherName("Test Publisher");

        assertNotNull(books);
        assertEquals(2, books.size());
        assertEquals("Book 1", books.get(0).getTitle());
        assertEquals("Book 2", books.get(1).getTitle());

        verify(publisherRepository, times(1)).findPublisherByName("Test Publisher");
        verify(bookRepository, times(1)).findBooksByPublisherId(1L);
    }
    @Test
    void testGetBooksByPublisherNameNotFound() {
        when(publisherRepository.findPublisherByName("Nonexistent Publisher")).thenReturn(Collections.emptyList());

        assertThrows(PublisherNotFoundException.class, () -> publisherService.getBooksByPublisherName("Nonexistent Publisher"));

        verify(publisherRepository, times(1)).findPublisherByName("Nonexistent Publisher");
    }
    @Test
    void testGetBooksByPublisherCountry() {
        Publisher publisher = new Publisher();
        publisher.setPublisher_id(1L);
        publisher.setCountry("Test Country");

        Book book1 = new Book();
        book1.setBook_id(1L);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setBook_id(2L);
        book2.setTitle("Book 2");

        when(publisherRepository.findPublisherByCountry("Test Country")).thenReturn(List.of(publisher));
        when(bookRepository.findBooksByPublisherId(1L)).thenReturn(List.of(book1, book2));

        List<Book> books = publisherService.getBooksByPublisherCountry("Test Country");

        assertNotNull(books);
        assertEquals(2, books.size());
        assertEquals("Book 1", books.get(0).getTitle());
        assertEquals("Book 2", books.get(1).getTitle());

        verify(publisherRepository, times(1)).findPublisherByCountry("Test Country");
        verify(bookRepository, times(1)).findBooksByPublisherId(1L);
    }
    @Test
    void testGetBooksByPublisherCountryNotFound() {
        when(publisherRepository.findPublisherByCountry("Nonexistent Country")).thenReturn(Collections.emptyList());

        assertThrows(PublisherNotFoundException.class, () -> publisherService.getBooksByPublisherCountry("Nonexistent Country"));

        verify(publisherRepository, times(1)).findPublisherByCountry("Nonexistent Country");
    }
}