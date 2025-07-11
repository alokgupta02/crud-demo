package com.book.demo.service;

import com.book.demo.dto.BookDto;
import org.springframework.data.domain.Page;

public interface BookService {
    Page<BookDto> getAllBooks(int page, int size, String[] sort);
    BookDto getBookById(Long id);
    BookDto createBook(BookDto bookDto);
    BookDto updateBook(Long id, BookDto bookDto);
    void deleteBook(Long id);
}
