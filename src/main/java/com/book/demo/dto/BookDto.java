package com.book.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for Book.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Author is mandatory")
    private String author;

    @Min(value = 1, message = "Pages must be at least 1")
    private int pages;

    @NotNull(message = "Published year is required")
    @Min(value = 1000, message = "Year must be valid")
    private Integer publishedYear;
}
