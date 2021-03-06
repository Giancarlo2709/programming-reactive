package io.spring.boot.exchange.rate.controller.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRateRequest {

    private Double amount;
    private String currencyIsoSource;
    private String currencyIsoTarget;
}
