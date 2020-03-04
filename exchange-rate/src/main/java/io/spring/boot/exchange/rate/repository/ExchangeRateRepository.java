package io.spring.boot.exchange.rate.repository;

import io.spring.boot.exchange.rate.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    ExchangeRate findByCurrencyIsoSourceAndCurrencyIsoTarget(String currencyIsoSource, String currencyIsoTarget);

}
