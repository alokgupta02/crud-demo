package com.book.demo.service.impl;

import com.book.demo.dto.BookDto;
import com.book.demo.model.Book;
import com.book.demo.repository.BookRepository;
import com.book.demo.service.BookService;
import com.book.demo.mapper.ObjectMapper;
import com.book.demo.exception.BookNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Book CRUD operations, pagination, and sorting.
 */
@Service
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    /**
     * Returns a paginated and sorted list of BookDto.
     * @param page page number (0-based)
     * @param size page size
     * @param sort array of sort fields, e.g. {"title,asc", "author,desc"}
     * @return paginated list of BookDto
     */
    @Override
    public Page<BookDto> getAllBooks(int page, int size, String[] sort) {
        logger.debug("Fetching books: page={}, size={}, sort={}", page, size, String.join(";", sort));
        Sort sortObj = Sort.by(
            Arrays.stream(sort)
                .map(s -> {
                    String[] parts = s.split(",");
                    return parts.length == 2 && parts[1].equalsIgnoreCase("desc") ?
                        new Sort.Order(Sort.Direction.DESC, parts[0]) :
                        new Sort.Order(Sort.Direction.ASC, parts[0]);
                })
                .toList()
        );
        var pageable = PageRequest.of(page, size, sortObj);
        var bookPage = bookRepository.findAll(pageable);
        var dtoList = bookPage.getContent().stream().map(ObjectMapper::toDto).toList();
        return new PageImpl<>(dtoList, pageable, bookPage.getTotalElements());
    }

    /**
     * Returns a BookDto by its ID.
     * @param id book ID
     * @return Optional of BookDto
     */
    @Override
    public BookDto getBookById(Long id) {
        logger.debug("Fetching book by id={}", id);
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return ObjectMapper.toDto(book);
    }

    /**
     * Creates a new book.
     * @param bookDto book data
     * @return created BookDto
     */
    @Override
    public BookDto createBook(BookDto bookDto) {
        logger.debug("Creating book: {}", bookDto);
        Book book = ObjectMapper.toEntity(bookDto);
        return ObjectMapper.toDto(bookRepository.save(book));
    }

    /**
     * Updates an existing book by ID.
     * @param id book ID
     * @param bookDto updated book data
     * @return updated BookDto
     */
    @Override
    public BookDto updateBook(Long id, BookDto bookDto) {
        logger.debug("Updating book id={}: {}", id, bookDto);
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPages(bookDto.getPages());
        book.setPublishedYear(bookDto.getPublishedYear());
        return ObjectMapper.toDto(bookRepository.save(book));
    }

    /**
     * Deletes a book by ID.
     * @param id book ID
     */
    @Override
    public void deleteBook(Long id) {
        logger.debug("Deleting book id={}", id);
        bookRepository.deleteById(id);
    }
}
