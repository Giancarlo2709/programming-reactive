package com.spring.boot.reactive.service.impl;

import com.spring.boot.reactive.model.Author;
import com.spring.boot.reactive.model.Book;
import com.spring.boot.reactive.repository.AuthorRepository;
import com.spring.boot.reactive.repository.BookRepository;
import com.spring.boot.reactive.service.BookService;
import com.spring.boot.reactive.service.dto.AddBookRequest;
import com.spring.boot.reactive.service.dto.BookResponse;
import com.spring.boot.reactive.service.dto.UpdateBookRequest;
import io.reactivex.rxjava3.core.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Single<Integer> addBook(AddBookRequest addBookRequest) {

        Single<Integer> authorId = Single.just(authorRepository.findById(addBookRequest.getAuthorId()))
                .flatMap(s -> Single.just(s.get().getId()));

        return Single.just(bookRepository.save(toBook(addBookRequest))
                .getId())
                .doOnError(throwable -> new EntityNotFoundException());
    }

    private Book toBook(AddBookRequest addBookRequest) {
        return Book
                .builder()
                .author(Author.builder()
                        .id(addBookRequest.getAuthorId())
                        .build())
                .title(addBookRequest.getTitle())
                .build();
    }

    @Override
    public Completable updateBook(UpdateBookRequest updateBookRequest) {
        return updateBookToRepository(updateBookRequest);
    }

    private Completable updateBookToRepository(UpdateBookRequest updateBookRequest) {
        return Completable.create(emitter -> {
            Optional<Book> optionalBook = bookRepository.findById(updateBookRequest.getId());
            if(!optionalBook.isPresent()) {
                emitter.onError(new EntityNotFoundException());
            } else {
                Book book = optionalBook.get();
                book.setTitle(updateBookRequest.getTitle());
                bookRepository.save(book);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Flowable<BookResponse> getAllBooks(int limit, int page) {
        return findAllBooksInRepository(limit, page)
                .map(this::toBookResponse);
    }

    private Flowable<Book> findAllBooksInRepository(int limit, int page) {
        System.out.println("limit: " + limit  + " page: " + page);
        return Flowable.fromIterable( bookRepository.findAll(PageRequest.of(page, limit)).getContent());
    }

    private List<BookResponse> toBookResponseList(List<Book> books) {
        return books.stream()
                .map(this::toBookResponse)
                .collect(Collectors.toList());
    }

    private BookResponse toBookResponse(Book book) {
        BookResponse bookResponse = new BookResponse();
        BeanUtils.copyProperties(book, bookResponse);
        bookResponse.setAuthorName(book.getAuthor().getName());
        return bookResponse;
    }

    @Override
    public Single<BookResponse> getBookDetail(Integer id) {
        return findBookDetailInRepository(id);
    }

    private Single<BookResponse> findBookDetailInRepository(Integer id) {
        return Single.create(emitter -> {
           Optional<Book> book = bookRepository.findById(id);
           if(!book.isPresent()){
                emitter.onError(new EntityNotFoundException());
           } else {
                BookResponse bookResponse = toBookResponse(book.get());
                emitter.onSuccess(bookResponse);
           }
        });
    }

    @Override
    public Completable deleteBook(Integer id) {
        return null;
    }

    private Completable deleteBookInRepository(Integer id) {
        return Completable.create(emitter -> {
           Optional<Book> book = bookRepository.findById(id);
           if(!book.isPresent()) {
               emitter.onError(new EntityNotFoundException());
           } else {
                bookRepository.delete(book.get());
                emitter.onComplete();
           }
        });
    }
}
