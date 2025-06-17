package com.book.demo.mapper;

import com.book.demo.dto.BookDto;
import com.book.demo.model.Book;

public class ObjectMapper {
    public static BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPages(book.getPages());
        dto.setPublishedYear(book.getPublishedYear());
        return dto;
    }

    public static Book toEntity(BookDto dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPages(dto.getPages());
        book.setPublishedYear(dto.getPublishedYear());
        return book;
    }
}
