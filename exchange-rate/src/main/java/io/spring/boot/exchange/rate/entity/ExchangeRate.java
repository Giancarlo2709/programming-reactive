package io.spring.boot.exchange.rate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "currency_exchange")
@Entity
public class ExchangeRate {

    @Id
    private Long id;

    @Column(name = "source_currency")
    private String currencyIsoSource;

    @Column(name = "target_currency")
    private String currencyIsoTarget;

    @Column(name = "change")
    private Double change;
}
