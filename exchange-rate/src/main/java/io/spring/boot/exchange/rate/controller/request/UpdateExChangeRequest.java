package io.spring.boot.exchange.rate.controller.request;

import lombok.Data;

@Data
public class UpdateExChangeRequest {

    private String currencyIsoSource;
    private String currencyIsoTarget;
    private Double exchangeRate;
}
