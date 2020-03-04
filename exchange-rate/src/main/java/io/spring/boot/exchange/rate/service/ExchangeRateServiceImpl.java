package io.spring.boot.exchange.rate.service;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.spring.boot.exchange.rate.controller.request.ExchangeRateRequest;
import io.spring.boot.exchange.rate.controller.request.UpdateExChangeRequest;
import io.spring.boot.exchange.rate.controller.response.ExchangeRateResponse;
import io.spring.boot.exchange.rate.entity.ExchangeRate;
import io.spring.boot.exchange.rate.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public Flowable<ExchangeRateResponse> converter(ExchangeRateRequest exchangeRateRequests) {
        return Flowable.just(
                exchangeRateRepository.findByCurrencyIsoSourceAndCurrencyIsoTarget(exchangeRateRequests.getCurrencyIsoSource(),
                        exchangeRateRequests.getCurrencyIsoTarget()))
                .flatMap(exchange -> {
                    Double amountWithChange = "PEN".equals(exchangeRateRequests.getCurrencyIsoSource()) ?
                            exchangeRateRequests.getAmount() / exchange.getChange() :
                            exchangeRateRequests.getAmount() * exchange.getChange();

                    return Flowable.just(
                            ExchangeRateResponse.builder()
                                    .amount(exchangeRateRequests.getAmount())
                                    .amountWithExchangeRate(amountWithChange)
                                    .currencyIsoSource(exchangeRateRequests.getCurrencyIsoSource())
                                    .currencyIsoTarget(exchangeRateRequests.getCurrencyIsoTarget())
                                    .exchangeRate(exchange.getChange())
                                    .build()
                    );
                });
    }

    @Override
    public Completable update(UpdateExChangeRequest updateExChangeRequest) {
        return Observable.just(exchangeRateRepository.findByCurrencyIsoSourceAndCurrencyIsoTarget(
                updateExChangeRequest.getCurrencyIsoSource(),
                updateExChangeRequest.getCurrencyIsoTarget()
                ))
                .flatMapCompletable(s -> Completable.fromRunnable(() -> {
                    s.setChange(updateExChangeRequest.getExchangeRate());
                    exchangeRateRepository.save(s);
                }));
    }

    @Override
    public Flowable<ExchangeRate> findAll() {
        return Flowable.fromIterable(exchangeRateRepository.findAll());
    }
}
