package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.AuthorRequest;
import com.example.bookdbbackend.dtos.BookRequest;
import com.example.bookdbbackend.dtos.InventoryRequest;
import com.example.bookdbbackend.dtos.PublisherRequest;
import com.example.bookdbbackend.exception.BookNotFoundException;
import com.example.bookdbbackend.exception.InvalidDataException;
import com.example.bookdbbackend.model.*;
import com.example.bookdbbackend.repository.*;
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
    private BookJpRepository bookJpRepository;

    @Mock
    private BookFaRepository bookFaRepository;

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
    void testGetAllBooks_JapaneseLanguage() {
        List<BookJp> books = Arrays.asList(new BookJp(), new BookJp());
        when(bookJpRepository.findAll()).thenReturn(books);

        List<?> result = bookService.getAllBooks("ja");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookJpRepository, times(1)).findAll();
        verify(bookFaRepository, never()).findAll();
        verify(bookRepository, never()).findAll();
    }

    @Test
    void testGetAllBooks_FarsiLanguage() {
        List<BookFa> books = Arrays.asList(new BookFa(), new BookFa());
        when(bookFaRepository.findAll()).thenReturn(books);

        List<?> result = bookService.getAllBooks("fa");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookFaRepository, times(1)).findAll();
        verify(bookJpRepository, never()).findAll();
        verify(bookRepository, never()).findAll();
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
        bookRequest.setAuthors(Collections.singletonList(authorRequest)); // Initialize authors list

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
    @Test
    void testCreateBook_WithExistingPublisher() {
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
        bookRequest.setPublisherId(1L);

        // Initialize authors list
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
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));

        Author author = new Author();
        when(authorRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Book book = new Book();
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.createBook(bookRequest);

        assertNotNull(result);
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
        verify(publisherRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(any(Author.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

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

    @Test
    void testUpdateBook() {
        Long bookId = 1L;
        Book existingBook = new Book();
        Inventory inventory = new Inventory();
        Publisher publisher = new Publisher();
        existingBook.setInventory(inventory);
        existingBook.setPublisher(publisher);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "Updated Title");
        updates.put("isbn", "Updated ISBN");
        updates.put("genre", "Updated Genre");
        updates.put("type", "Updated Type");
        updates.put("price", BigDecimal.valueOf(29.99));
        updates.put("publication_year", 2022);
        updates.put("book_condition", "Updated Condition");
        updates.put("image_url", "http://example.com/updated_image.jpg");
        updates.put("reserved", true);

        Map<String, Integer> inventoryUpdates = new HashMap<>();
        inventoryUpdates.put("stockLevelUsed", 20);
        inventoryUpdates.put("stockLevelNew", 10);
        inventoryUpdates.put("reservedStock", 5);
        updates.put("inventory", inventoryUpdates);

        Map<String, String> publisherUpdates = new HashMap<>();
        publisherUpdates.put("name", "Updated Publisher");
        publisherUpdates.put("country", "Updated Country");
        updates.put("publisher", publisherUpdates);

        List<Map<String, Object>> authorUpdates = new ArrayList<>();
        Map<String, Object> authorUpdate = new HashMap<>();
        authorUpdate.put("author_id", 1L);
        authorUpdate.put("firstName", "Updated First Name");
        authorUpdate.put("lastName", "Updated Last Name");
        authorUpdates.add(authorUpdate);
        updates.put("authors", authorUpdates);

        Author author = new Author();
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Book updatedBook = bookService.updateBook(updates, bookId);

        assertNotNull(updatedBook);
        assertEquals("Updated Title", updatedBook.getTitle());
        assertEquals("Updated ISBN", updatedBook.getIsbn());
        assertEquals("Updated Genre", updatedBook.getGenre());
        assertEquals("Updated Type", updatedBook.getType());
        assertEquals(BigDecimal.valueOf(29.99), updatedBook.getPrice());
        assertEquals(2022, updatedBook.getPublication_year());
        assertEquals("Updated Condition", updatedBook.getBook_condition());
        assertEquals("http://example.com/updated_image.jpg", updatedBook.getImage_url());
        assertTrue(updatedBook.isReserved());

        assertEquals(20, updatedBook.getInventory().getStock_level_used());
        assertEquals(10, updatedBook.getInventory().getStock_level_new());
        assertEquals(5, updatedBook.getInventory().getReserved_stock());

        assertEquals("Updated Publisher", updatedBook.getPublisher().getName());
        assertEquals("Updated Country", updatedBook.getPublisher().getCountry());

        assertEquals("Updated First Name", author.getFirstName());
        assertEquals("Updated Last Name", author.getLastName());

        verify(bookRepository, times(2)).findById(bookId);
        verify(bookRepository, times(1)).save(existingBook);
        verify(inventoryRepository, times(1)).save(inventory);
        verify(publisherRepository, times(1)).save(publisher);
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(author);
    }


    @Test
    void testUpdateBook_BookNotFound() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Map<String, Object> updates = new HashMap<>();

        assertThrows(InvalidDataException.class, () -> bookService.updateBook(updates, bookId));
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }
    @Test
    void testCreateDummyBook() {
        BookRequest dummyBook = bookService.createDummyBook();

        assertNotNull(dummyBook);
        assertEquals("", dummyBook.getTitle());
        assertEquals("", dummyBook.getIsbn());
        assertEquals("", dummyBook.getGenre());
        assertEquals("", dummyBook.getType());
        assertEquals(0, dummyBook.getPublicationYear());
        assertEquals(BigDecimal.valueOf(0), dummyBook.getPrice());
        assertEquals("", dummyBook.getBookCondition());
        assertFalse(dummyBook.isReserved());
        assertEquals("", dummyBook.getImageUrl());

        assertNotNull(dummyBook.getPublisher());
        assertEquals("", dummyBook.getPublisher().getName());
        assertEquals("", dummyBook.getPublisher().getCountry());

        assertNotNull(dummyBook.getAuthors());
        assertTrue(dummyBook.getAuthors().isEmpty());

        assertNotNull(dummyBook.getInventory());
        assertEquals(0, dummyBook.getInventory().getStockLevelUsed());
        assertEquals(0, dummyBook.getInventory().getStockLevelNew());
        assertEquals(0, dummyBook.getInventory().getReservedStock());
    }
    @Test
    void testGetBooksByTitle_BooksFound() {
        String title = "Test Title";
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findBooksByTitleContainingIgnoreCase(title)).thenReturn(books);

        List<Book> result = bookService.getBooksByTitle(title);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findBooksByTitleContainingIgnoreCase(title);
    }

    @Test
    void testGetBooksByTitle_BooksNotFound() {
        String title = "Nonexistent Title";
        when(bookRepository.findBooksByTitleContainingIgnoreCase(title)).thenReturn(Collections.emptyList());

        assertThrows(BookNotFoundException.class, () -> bookService.getBooksByTitle(title));
        verify(bookRepository, times(1)).findBooksByTitleContainingIgnoreCase(title);
    }
    @Test
    void testGetBooksByAuthor_BooksFound() {
        String author = "John Doe";
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findBooksByAuthorsFirstNameContainingIgnoreCase(author)).thenReturn(books);

        List<Book> result = bookService.getBooksByAuthor(author);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findBooksByAuthorsFirstNameContainingIgnoreCase(author);
    }

    @Test
    void testGetBooksByAuthor_BooksNotFound() {
        String author = "Nonexistent Author";
        when(bookRepository.findBooksByAuthorsFirstNameContainingIgnoreCase(author)).thenReturn(Collections.emptyList());

        assertThrows(BookNotFoundException.class, () -> bookService.getBooksByAuthor(author));
        verify(bookRepository, times(1)).findBooksByAuthorsFirstNameContainingIgnoreCase(author);
    }
    @Test
    void testGetBooksByGenre_BooksFound() {
        String genre = "Fiction";
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findBooksByGenreContainingIgnoreCase(genre)).thenReturn(books);

        List<Book> result = bookService.getBooksByGenre(genre);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findBooksByGenreContainingIgnoreCase(genre);
    }

    @Test
    void testGetBooksByGenre_BooksNotFound() {
        String genre = "Nonexistent Genre";
        when(bookRepository.findBooksByGenreContainingIgnoreCase(genre)).thenReturn(Collections.emptyList());

        assertThrows(BookNotFoundException.class, () -> bookService.getBooksByGenre(genre));
        verify(bookRepository, times(1)).findBooksByGenreContainingIgnoreCase(genre);
    }
    @Test
    void testGetBookByIsbn_BookFound() {
        String isbn = "1234567890";
        Book book = new Book();
        when(bookRepository.findBookByIsbn(isbn)).thenReturn(Optional.of(book));

        Book result = bookService.getBookByIsbn(isbn);

        assertNotNull(result);
        assertEquals(book, result);
        verify(bookRepository, times(1)).findBookByIsbn(isbn);
    }

    @Test
    void testGetBookByIsbn_BookNotFound() {
        String isbn = "Nonexistent ISBN";
        when(bookRepository.findBookByIsbn(isbn)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookByIsbn(isbn));
        verify(bookRepository, times(1)).findBookByIsbn(isbn);
    }
    @Test
    void testGetBooksByPublisherName_BooksFound() {
        String publisherName = "Test Publisher";
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findBooksByPublisherName(publisherName)).thenReturn(books);

        List<Book> result = bookService.getBooksByPublisherName(publisherName);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findBooksByPublisherName(publisherName);
    }

    @Test
    void testGetBooksByPublisherName_BooksNotFound() {
        String publisherName = "Nonexistent Publisher";
        when(bookRepository.findBooksByPublisherName(publisherName)).thenReturn(Collections.emptyList());

        assertThrows(BookNotFoundException.class, () -> bookService.getBooksByPublisherName(publisherName));
        verify(bookRepository, times(1)).findBooksByPublisherName(publisherName);
    }
    @Test
    void testSearchBooks_ValidSearchTerm() {
        String searchTerm = "Test";
        List<Book> booksByTitle = Arrays.asList(new Book(), new Book());
        List<Book> booksByPublisher = Arrays.asList(new Book());
        List<Book> booksByGenre = Collections.emptyList();
        List<Book> booksByIsbn = Collections.emptyList();
        List<Book> booksByAuthor = Collections.emptyList();
        List<Book> booksByAuthorLastName = Collections.emptyList();

        when(bookRepository.findBooksByTitleContainingIgnoreCase(searchTerm)).thenReturn(booksByTitle);
        when(bookRepository.findBooksByPublisherName(searchTerm)).thenReturn(booksByPublisher);
        when(bookRepository.findBooksByGenreContainingIgnoreCase(searchTerm)).thenReturn(booksByGenre);
        when(bookRepository.findBooksByIsbnContainingIgnoreCase(searchTerm)).thenReturn(booksByIsbn);
        when(bookRepository.findBooksByAuthorsFirstNameContainingIgnoreCase(searchTerm)).thenReturn(booksByAuthor);
        when(bookRepository.findBooksByAuthorsLastNameContainingIgnoreCase(searchTerm)).thenReturn(booksByAuthorLastName);

        List<Book> result = bookService.searchBooks(searchTerm);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(bookRepository, times(1)).findBooksByTitleContainingIgnoreCase(searchTerm);
        verify(bookRepository, times(1)).findBooksByPublisherName(searchTerm);
        verify(bookRepository, times(1)).findBooksByGenreContainingIgnoreCase(searchTerm);
        verify(bookRepository, times(1)).findBooksByIsbnContainingIgnoreCase(searchTerm);
        verify(bookRepository, times(1)).findBooksByAuthorsFirstNameContainingIgnoreCase(searchTerm);
        verify(bookRepository, times(1)).findBooksByAuthorsLastNameContainingIgnoreCase(searchTerm);
    }

    @Test
    void testSearchBooks_ShortSearchTerm() {
        String searchTerm = "Te";

        assertThrows(IllegalArgumentException.class, () -> bookService.searchBooks(searchTerm));
        verify(bookRepository, never()).findBooksByTitleContainingIgnoreCase(anyString());
        verify(bookRepository, never()).findBooksByPublisherName(anyString());
        verify(bookRepository, never()).findBooksByGenreContainingIgnoreCase(anyString());
        verify(bookRepository, never()).findBooksByIsbnContainingIgnoreCase(anyString());
        verify(bookRepository, never()).findBooksByAuthorsFirstNameContainingIgnoreCase(anyString());
        verify(bookRepository, never()).findBooksByAuthorsLastNameContainingIgnoreCase(anyString());
    }

    @Test
    void testGetBooksByAuthor() {
        String authorFirstName = "John";
        String authorLastName = "Doe";


        Author author = new Author();
        author.setFirstName(authorFirstName);
        author.setLastName(authorLastName);

        Book book1 = new Book();
        book1.setTitle("Book One");
        Book book2 = new Book();
        book2.setTitle("Book Two");


        when(bookRepository.findBooksByAuthorsFirstNameContainingIgnoreCase(authorFirstName)).thenReturn(Arrays.asList(book1, book2));

        List<Book> result = bookService.getBooksByAuthor(authorFirstName);


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Book One", result.get(0).getTitle());
        assertEquals("Book Two", result.get(1).getTitle());

        verify(bookRepository, times(1)).findBooksByAuthorsFirstNameContainingIgnoreCase(authorFirstName);
    }





    @Test
    void testGetBooksByAuthor_NotFound() {
        String authorFirstName = "John";
        String authorLastName = "Doe";


        when(bookRepository.findBooksByAuthorsFirstNameContainingIgnoreCase(authorFirstName))
                .thenReturn(Collections.emptyList());


        assertThrows(BookNotFoundException.class, () -> bookService.getBooksByAuthor(authorFirstName));


        verify(bookRepository, times(1)).findBooksByAuthorsFirstNameContainingIgnoreCase(authorFirstName);
    }


    @Test
    void testGetBooksByGenre() {
        String genre = "Fiction";


        Book book1 = new Book();
        book1.setGenre(genre);
        Book book2 = new Book();
        book2.setGenre(genre);


        when(bookRepository.findBooksByGenreContainingIgnoreCase(genre)).thenReturn(Arrays.asList(book1, book2));

        List<Book> result = bookService.getBooksByGenre(genre);


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(genre, result.get(0).getGenre());
        assertEquals(genre, result.get(1).getGenre());


        verify(bookRepository, times(1)).findBooksByGenreContainingIgnoreCase(genre);
    }

}