package com.example.bookdbackend.controller;

import com.example.bookdbbackend.controller.AuthorController;
import com.example.bookdbbackend.model.Author;
import com.example.bookdbbackend.service.IAuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthorControllerTest {

    @Mock
    private IAuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAuthors() {
        List<Author> authors = Arrays.asList(new Author(), new Author());
        when(authorService.getAllAuthors()).thenReturn(authors);

        ResponseEntity<List<Author>> response = authorController.getAllAuthors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authors, response.getBody());
        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    void testGetAuthorById() {
        Long id = 1L;
        Author author = new Author();
        when(authorService.getAuthorById(id)).thenReturn(author);

        ResponseEntity<Author> response = authorController.getAuthorById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(author, response.getBody());
        verify(authorService, times(1)).getAuthorById(id);
    }
}