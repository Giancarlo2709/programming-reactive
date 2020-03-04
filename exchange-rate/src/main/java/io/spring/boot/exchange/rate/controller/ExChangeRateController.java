package io.spring.boot.exchange.rate.controller;

import io.reactivex.Maybe;
import io.spring.boot.exchange.rate.controller.request.ExchangeRateRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/exchange/rate")
public class ExChangeRateController {

    @PostMapping("/calculator")
    public Maybe<BigDecimal> calculator(@RequestBody ExchangeRateRequest exchangeRateRequest) {
        return Maybe.just(exchangeRateRequest.getAmount());
    }

}
