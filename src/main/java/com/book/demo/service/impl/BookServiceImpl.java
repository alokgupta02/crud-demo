package com.book.demo.service.impl;

import com.book.demo.dto.BookDto;
import com.book.demo.model.Book;
import com.book.demo.repository.BookRepository;
import com.book.demo.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    private BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPages(book.getPages());
        dto.setPublishedYear(book.getPublishedYear());
        return dto;
    }

    private Book toEntity(BookDto dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPages(dto.getPages());
        book.setPublishedYear(dto.getPublishedYear());
        return book;
    }

    @Override
    public Optional<BookDto> getBookById(Long id) {
        return bookRepository.findById(id).map(this::toDto);
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = toEntity(bookDto);
        return toDto(bookRepository.save(book));
    }

    @Override
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPages(bookDto.getPages());
        book.setPublishedYear(bookDto.getPublishedYear());
        return toDto(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> getAllBooks(int page, int size) {
        var pageable = PageRequest.of(page, size);
        var bookPage = bookRepository.findAll(pageable);
        var dtoList = bookPage.getContent().stream().map(this::toDto).toList();
        return new PageImpl<>(dtoList, pageable, bookPage.getTotalElements());
    }
}
