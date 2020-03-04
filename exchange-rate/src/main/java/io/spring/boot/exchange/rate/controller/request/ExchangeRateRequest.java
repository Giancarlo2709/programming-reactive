package io.spring.boot.exchange.rate.controller.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRateRequest {

    private BigDecimal amount;
    private String currencyIsoOrigin;
    private String currencyIsoDestination;
}
