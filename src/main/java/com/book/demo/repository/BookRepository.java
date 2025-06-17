package com.book.demo.repository;

import com.book.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Book entity.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
