package com.spring.boot.reactive.repository;

import com.spring.boot.reactive.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByAuthorId(Integer authorId);

}
