package io.spring.boot.exchange.rate.controller;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.spring.boot.exchange.rate.controller.request.ExchangeRateRequest;
import io.spring.boot.exchange.rate.controller.request.UpdateExChangeRequest;
import io.spring.boot.exchange.rate.controller.response.ExchangeRateResponse;
import io.spring.boot.exchange.rate.entity.ExchangeRate;
import io.spring.boot.exchange.rate.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exchange")
public class ExChangeRateController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExChangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @PostMapping("/converter")
    public Flowable<ExchangeRateResponse> converter(@RequestBody ExchangeRateRequest exchangeRateRequest) {
        return exchangeRateService.converter(exchangeRateRequest);
    }

    @PutMapping
    public Completable update(@RequestBody UpdateExChangeRequest updateExChangeRequest) {
        return exchangeRateService.update(updateExChangeRequest);
    }

    @GetMapping
    public Flowable<ExchangeRate> findAll(){
        return exchangeRateService.findAll();
    }

}
