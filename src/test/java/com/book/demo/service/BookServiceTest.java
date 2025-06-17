package com.book.demo.service;

import com.book.demo.dto.BookDto;
import com.book.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.book.demo.service.impl.BookServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("Author");
        bookDto.setPages(100);
        bookDto.setPublishedYear(2020);
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList());
        List<BookDto> books = bookService.getAllBooks();
        assertNotNull(books);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<BookDto> found = bookService.getBookById(1L);
        assertTrue(found.isEmpty());
    }

    @Test
    void testCreateBook() {
        when(bookRepository.save(any())).thenReturn(null);
        BookDto created = bookService.createBook(bookDto);
        assertNotNull(created);
    }

    @Test
    void testUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookService.updateBook(1L, bookDto));
    }

    @Test
    void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(1L);
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }
}
