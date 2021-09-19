package ru.otus.reval.consumer.currencyRate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.reval.domain.CurrencyEntity;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CurrencyRateDtoList {
    private List<CurrencyRateDto> currencyRateDtoList;
}
