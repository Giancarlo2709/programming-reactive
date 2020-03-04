package com.spring.boot.reactive.service;

import com.spring.boot.reactive.service.dto.AddBookRequest;
import com.spring.boot.reactive.service.dto.BookResponse;
import com.spring.boot.reactive.service.dto.UpdateBookRequest;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface BookService {

    Single<Integer> addBook(AddBookRequest addBookRequest);

    Completable updateBook(UpdateBookRequest updateBookRequest);

    Flowable<BookResponse> getAllBooks(int limit, int page);

    Single<BookResponse> getBookDetail(Integer id);

    Completable deleteBook(Integer id);
}
