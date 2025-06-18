package com.book.demo.controller;

import com.book.demo.dto.BookDto;
import com.book.demo.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("Author");
        bookDto.setPages(100);
        bookDto.setPublishedYear(2020);
    }

    @Test
    void testGetAllBooks() throws Exception {
        when(bookService.getAllBooks(anyInt(), anyInt(), any(String[].class))).thenReturn(new org.springframework.data.domain.PageImpl<>(Arrays.asList(bookDto)));
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(bookDto);
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateBook() throws Exception {
        when(bookService.createBook(any(BookDto.class))).thenReturn(bookDto);
        String json = "{\"title\":\"Test Book\",\"author\":\"Author\",\"pages\":100,\"publishedYear\":2020}";
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateBook() throws Exception {
        when(bookService.updateBook(eq(1L), any(BookDto.class))).thenReturn(bookDto);
        String json = "{\"title\":\"Test Book\",\"author\":\"Author\",\"pages\":100,\"publishedYear\":2020}";
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }
}
