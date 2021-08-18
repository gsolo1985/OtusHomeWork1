package ru.otus.reval.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.reval.domain.CurrencyEntity;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CurrencyRateDto {
    private String currencyPair;
    private BigDecimal rate;
    private String date;
    private CurrencyEntity buyCurrency;
    private CurrencyEntity sellCurrency;
}
