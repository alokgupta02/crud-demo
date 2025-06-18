package com.book.demo.controller;

import com.book.demo.dto.BookDto;
import com.book.demo.service.BookService;
import com.book.demo.response.ApiResponse;
import com.book.demo.constants.ApiMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for Book CRUD operations.
 */
@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Book API", description = "CRUD operations for books with pagination and sorting")
public class BookController {
    private final BookService bookService;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(
        summary = "Get paginated and sorted list of books",
        description = "Returns a paginated and sorted list of books. Use 'page', 'size', and 'sort' query parameters. Example: /api/books?page=0&size=5&sort=title,asc"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookDto>>> getAllBooks(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort fields, e.g. sort=title,asc&sort=author,desc") @RequestParam(defaultValue = "id,asc") String[] sort,
            HttpServletRequest request) {
        if (logger.isInfoEnabled()) {
            logger.info("Fetching books: page={}, size={}, sort={}", page, size, String.join(";", sort));
        }
        Page<BookDto> data = bookService.getAllBooks(page, size, sort);
        ApiResponse<Page<BookDto>> response = new ApiResponse<>(
            true,
            ApiMessages.BOOK_FETCH_SUCCESS.getMessage(),
            data,
            null,
            0,
            request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get a book by ID",
        description = "Returns a single book by its ID. Returns 404 if not found."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDto>> getBookById(@PathVariable Long id, HttpServletRequest request) {
        logger.info("Fetching book with id={}", id);
        BookDto bookDto = bookService.getBookById(id);
        ApiResponse<BookDto> response = new ApiResponse<>(
            true,
            ApiMessages.BOOK_FETCH_SUCCESS.getMessage(),
            bookDto,
            null,
            0,
            request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Create a new book",
        description = "Creates a new book. Requires a valid BookDto in the request body."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<BookDto>> createBook(@Valid @RequestBody BookDto bookDto, HttpServletRequest request) {
        logger.info("Creating new book: {}", bookDto);
        BookDto created = bookService.createBook(bookDto);
        ApiResponse<BookDto> response = new ApiResponse<>(
            true,
            ApiMessages.BOOK_CREATE_SUCCESS.getMessage(),
            created,
            null,
            0,
            request.getRequestURI()
        );
        return ResponseEntity.status(201).body(response);
    }

    @Operation(
        summary = "Update an existing book",
        description = "Updates an existing book by ID. Returns 404 if not found."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDto>> updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDto, HttpServletRequest request) {
        logger.info("Updating book with id={}: {}", id, bookDto);
        BookDto updated = bookService.updateBook(id, bookDto);
        ApiResponse<BookDto> response = new ApiResponse<>(
            true,
            ApiMessages.BOOK_UPDATE_SUCCESS.getMessage(),
            updated,
            null,
            0,
            request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete a book",
        description = "Deletes a book by ID. Returns 204 No Content."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id, HttpServletRequest request) {
        logger.info("Deleting book with id={}", id);
        bookService.deleteBook(id);
        // No need to create ApiResponse since no content is returned
        return ResponseEntity.noContent().build();
    }
}
