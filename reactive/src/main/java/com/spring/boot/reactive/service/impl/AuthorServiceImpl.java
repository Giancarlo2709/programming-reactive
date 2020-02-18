package com.spring.boot.reactive.service.impl;

import com.spring.boot.reactive.model.Author;
import com.spring.boot.reactive.repository.AuthorRepository;
import com.spring.boot.reactive.service.AuthorService;
import com.spring.boot.reactive.service.dto.AddAuthorRequest;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Single<Integer> addAuthor(AddAuthorRequest addAuthorRequest) {
        return addAuthorToRepository(addAuthorRequest);
    }

    private Single<Integer> addAuthorToRepository(AddAuthorRequest addAuthorRequest) {
        return Single.just(authorRepository.save(Author
                .builder()
                .name(addAuthorRequest.getName())
                .build())
                .getId());
    }
}
