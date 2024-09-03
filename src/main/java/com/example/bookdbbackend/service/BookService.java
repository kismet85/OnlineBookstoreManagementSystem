package com.example.bookdbbackend.service;
import com.example.bookdbbackend.exception.BookAlreadyExistsException;
import com.example.bookdbbackend.exception.BookNotFoundException;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService implements IBookService {

    @Autowired
    private final BookRepository bookRepository;
    @Override
    public Book addBook(Book book) {
        if (bookAlreadyExists(book.getBook_id()))
        {
            throw new BookAlreadyExistsException("Book with id " + book.getBook_id() + " already exists");
        }
        return bookRepository.save(book);
    }

    private boolean bookAlreadyExists(Long id) {
        return bookRepository.findById(id).isPresent();
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public  Book getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (!book.isPresent()){
            throw new BookNotFoundException("Book with id " + id + " not found");
        }

        return book.get();
    }

    @Override
    public Book updateBook(Book book, Long id)
    {
        if (!bookAlreadyExists(id)){
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
        book.setBook_id(id);
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookAlreadyExists(id)){
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        List<Book> books = bookRepository.findBooksByTitleContainingIgnoreCase(title);
        if (books.isEmpty()){
            throw new BookNotFoundException("Book with title " + title + " not found");
        }
        return books;
    }

    @Override
    public List <Book> getBooksByAuthorId(Long author_id) {
        return bookRepository.findBooksByAuthorId(author_id);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        Optional<Book> book = bookRepository.findBookByIsbn(isbn);
        if (!book.isPresent()){
            throw new BookNotFoundException("Book with isbn " + isbn + " not found");
        }
        return book.get();
    }

    @Override
    public List<Book> getBooksByGenre(String genre) {
        List<Book> books = bookRepository.findBooksByGenreContainingIgnoreCase(genre);
        if (books.isEmpty()){
            throw new BookNotFoundException("Book with genre " + genre + " not found");
        }
        return books;
    }

    @Override
    public List<Book> getBooksByPublisherName(String publisher) {
        List<Book> books = bookRepository.findBooksByPublisherName(publisher);
        if (books.isEmpty()){
            throw new BookNotFoundException("Book with publisher " + publisher + " not found");
        }
        return books;
    }


}
