package io.spring.boot.exchange.rate.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeRateResponse {

    private Double amount;
    private Double amountWithExchangeRate;
    private String currencyIsoSource;
    private String currencyIsoTarget;
    private Double exchangeRate;
}
