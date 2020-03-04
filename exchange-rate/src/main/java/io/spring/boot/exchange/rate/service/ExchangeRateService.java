package io.spring.boot.exchange.rate.service;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.spring.boot.exchange.rate.controller.request.ExchangeRateRequest;
import io.spring.boot.exchange.rate.controller.request.UpdateExChangeRequest;
import io.spring.boot.exchange.rate.controller.response.ExchangeRateResponse;
import io.spring.boot.exchange.rate.entity.ExchangeRate;

import java.math.BigDecimal;

public interface ExchangeRateService {

    Flowable<ExchangeRateResponse> converter(ExchangeRateRequest exchangeRateRequests);

    Completable update(UpdateExChangeRequest updateExChangeRequest);

    Flowable<ExchangeRate> findAll();
}
