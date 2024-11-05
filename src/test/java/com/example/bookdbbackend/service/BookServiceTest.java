package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.AuthorRequest;
import com.example.bookdbbackend.dtos.BookRequest;
import com.example.bookdbbackend.dtos.InventoryRequest;
import com.example.bookdbbackend.dtos.PublisherRequest;
import com.example.bookdbbackend.exception.BookNotFoundException;
import com.example.bookdbbackend.model.Author;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Inventory;
import com.example.bookdbbackend.model.Publisher;
import com.example.bookdbbackend.repository.AuthorRepository;
import com.example.bookdbbackend.repository.BookRepository;
import com.example.bookdbbackend.repository.InventoryRepository;
import com.example.bookdbbackend.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBook() {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("Test Book");
        bookRequest.setIsbn("1234567890");
        bookRequest.setGenre("Fiction");
        bookRequest.setType("Hardcover");
        bookRequest.setPublicationYear(2021);
        bookRequest.setPrice(BigDecimal.valueOf(19.99));
        bookRequest.setBookCondition("New");
        bookRequest.setReserved(false);
        bookRequest.setImageUrl("http://example.com/image.jpg");

        PublisherRequest publisherRequest = new PublisherRequest();
        publisherRequest.setName("Test Publisher");
        publisherRequest.setCountry("USA");
        bookRequest.setPublisher(publisherRequest);

        AuthorRequest authorRequest = new AuthorRequest();
        authorRequest.setFirstName("John");
        authorRequest.setLastName("Doe");
        bookRequest.setAuthors(Collections.singletonList(authorRequest));

        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setStockLevelUsed(10);
        inventoryRequest.setStockLevelNew(5);
        inventoryRequest.setReservedStock(2);
        bookRequest.setInventory(inventoryRequest);

        Inventory inventory = new Inventory();
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        Publisher publisher = new Publisher();
        when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);

        Author author = new Author();
        when(authorRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Book book = new Book();
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.createBook(bookRequest);

        assertNotNull(result);
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
        verify(publisherRepository, times(1)).save(any(Publisher.class));
        verify(authorRepository, times(1)).save(any(Author.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

//    @Test
//    void testGetAllBooks() {
//        List<Book> books = Arrays.asList(new Book(), new Book());
//        when(bookRepository.findAll()).thenReturn(books);
//
//        List<Book> result = bookService.getAllBooks();
//
//        assertEquals(books, result);
//        verify(bookRepository, times(1)).findAll();
//    }

    @Test
    void testGetBookById_BookExists() {
        Long id = 1L;
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(id);

        assertEquals(book, result);
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    void testGetBookById_BookNotFound() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(id));
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteBook_BookExists() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.of(new Book()));

        bookService.deleteBook(id);

        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteBook_BookNotFound() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(id));
        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, never()).deleteById(id);
    }
}