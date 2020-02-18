package com.spring.boot.reactive.service;

import com.spring.boot.reactive.service.dto.AddAuthorRequest;
import io.reactivex.rxjava3.core.Single;

public interface AuthorService {

    Single<Integer> addAuthor(AddAuthorRequest addAuthorRequest);
}
