package ru.otus.reval.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateGet {
    private long id;
    private String buyCurrencyName;
    private Long buyCurrencyId;
    private String sellCurrencyName;
    private Long sellCurrencyId;
    private BigDecimal rate;
    private LocalDate date;
}
