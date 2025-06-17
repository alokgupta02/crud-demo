package com.book.demo.service;

import com.book.demo.dto.BookDto;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BookService {
    Page<BookDto> getAllBooks(int page, int size);
    Optional<BookDto> getBookById(Long id);
    BookDto createBook(BookDto bookDto);
    BookDto updateBook(Long id, BookDto bookDto);
    void deleteBook(Long id);
}
