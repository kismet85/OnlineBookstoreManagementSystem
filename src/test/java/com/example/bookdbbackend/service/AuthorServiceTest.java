package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.UserNotFoundException;
import com.example.bookdbbackend.model.Author;
import com.example.bookdbbackend.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAuthors() {
        List<Author> authors = Arrays.asList(new Author(), new Author());
        when(authorRepository.findAll()).thenReturn(authors);

        List<Author> result = authorService.getAllAuthors();

        assertEquals(authors, result);
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void testGetAuthorById_AuthorNotFound() {
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authorService.getAuthorById(id));
        verify(authorRepository, times(1)).findById(id);
    }
}