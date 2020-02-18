package com.spring.boot.reactive.rest;

import com.spring.boot.reactive.rest.request.UpdateBookWebRequest;
import com.spring.boot.reactive.rest.response.BaseWebResponse;
import com.spring.boot.reactive.rest.response.BookWebResponse;
import com.spring.boot.reactive.service.BookService;
import com.spring.boot.reactive.service.dto.AddBookRequest;
import com.spring.boot.reactive.service.dto.BookResponse;
import com.spring.boot.reactive.service.dto.UpdateBookRequest;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<BaseWebResponse>> addBook(
            @RequestBody AddBookRequest addBookRequest) {
        return bookService.addBook(addBookRequest)
                .subscribeOn(Schedulers.io())
                .map( s -> ResponseEntity
                        .created(URI.create("/api/books/" + s))
                        .body(BaseWebResponse.successNoData())
                );
    }

    @PutMapping(
            value = "/{bookId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<BaseWebResponse>> updateBook(@PathVariable Integer bookId,
                                                              @RequestBody UpdateBookWebRequest updateBookWebRequest) {
        return bookService.updateBook(toUpdateBookRequest(bookId, updateBookWebRequest))
                .subscribeOn(Schedulers.io())
                .toSingle(() -> ResponseEntity.ok(BaseWebResponse.successNoData()));
    }

    private UpdateBookRequest toUpdateBookRequest(Integer bookId, UpdateBookWebRequest updateBookWebRequest) {
        return UpdateBookRequest
                .builder()
                .id(bookId)
                .title(updateBookWebRequest.getTitle())
                .build();
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<BaseWebResponse<List<BookWebResponse>>>> getAllBooks(@RequestParam(value = "limit", defaultValue = "5") int limit,
                                                                                @RequestParam(value = "page", defaultValue = "0") int page) {
        return bookService.getAllBooks(limit, page)
                .subscribeOn(Schedulers.io())
                .map(bookResponses -> ResponseEntity
                        .ok(BaseWebResponse.successWithData(toBookWebResponseList(bookResponses))))
                .doOnSuccess(s -> System.out.println("get all"));
    }

    private List<BookWebResponse> toBookWebResponseList(List<BookResponse> bookResponses) {
        return bookResponses
                .stream()
                .map(this::toBookWebResponse)
                .collect(Collectors.toList());
    }

    private BookWebResponse toBookWebResponse(BookResponse bookResponse) {
        return BookWebResponse.builder()
                .id(bookResponse.getId())
                .title(bookResponse.getTitle())
                .authorName(bookResponse.getAuthorName())
                .build();
    }

    @GetMapping(
            value = "/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Single<ResponseEntity<BaseWebResponse>> getBookDetail(@PathVariable Integer bookId) {
        return bookService.getBookDetail(bookId)
                .subscribeOn(Schedulers.io())
                .map(bookResponse -> ResponseEntity.ok(BaseWebResponse.successWithData(toBookWebResponse(bookResponse))));
    }

    @DeleteMapping(value = "/{bookId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public Single<ResponseEntity<BaseWebResponse>> deleteBook(@PathVariable Integer bookId) {
        return bookService.deleteBook(bookId)
                .subscribeOn(Schedulers.io())
                .toSingle(() -> ResponseEntity.ok(BaseWebResponse.successNoData()));
    }

}
