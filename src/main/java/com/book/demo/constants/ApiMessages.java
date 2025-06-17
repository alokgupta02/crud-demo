package com.book.demo.constants;

public enum ApiMessages {
    BOOK_FETCH_SUCCESS("Book fetched successfully"),
    BOOK_CREATE_SUCCESS("Book created successfully"),
    BOOK_UPDATE_SUCCESS("Book updated successfully"),
    BOOK_DELETE_SUCCESS("Book deleted successfully"),
    BOOK_NOT_FOUND("Book not found"),
    INVALID_BOOK_DATA("Invalid book data"),
    INTERNAL_ERROR("Internal server error");

    private final String message;

    ApiMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
