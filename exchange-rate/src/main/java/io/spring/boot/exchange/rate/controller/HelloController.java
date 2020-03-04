package io.spring.boot.exchange.rate.controller;

import io.reactivex.Maybe;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @GetMapping
    public Maybe<String> hello() {
        return Maybe.just("Hello World");
    }
}
