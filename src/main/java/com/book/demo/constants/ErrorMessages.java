package com.book.demo.constants;

public enum ErrorMessages {
    BOOK_NOT_FOUND("E001", "Book not found"),
    INVALID_BOOK_DATA("E002", "Invalid book data"),
    INTERNAL_ERROR("E003", "Internal server error");

    private final String id;
    private final String message;

    ErrorMessages(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return id + " - " + message;
    }
}
