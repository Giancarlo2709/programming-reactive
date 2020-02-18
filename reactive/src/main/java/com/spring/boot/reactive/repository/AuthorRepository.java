package com.spring.boot.reactive.repository;

import com.spring.boot.reactive.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
