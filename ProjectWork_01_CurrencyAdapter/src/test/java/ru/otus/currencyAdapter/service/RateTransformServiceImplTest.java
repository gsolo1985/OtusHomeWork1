package ru.otus.currencyAdapter.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.currencyAdapter.publish.CurrencyRateDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit-тест преобразования формата layer в dto:")
class RateTransformServiceImplTest {

    @Test
    void layerTransformToDto() throws IOException {
        RateTransformService service = new RateTransformServiceImpl();
        String rate = "0.123";
        Map<String, Object> layerRequest = new HashMap<>();
        layerRequest.put("date", "2018-09-01");
        layerRequest.put("quotes", "{\"USDRUB\":0.123}");
        var rates = service.layerTransformToDto(layerRequest, 1);

        var target = CurrencyRateDto.builder()
                .currencyPair("USDRUB")
                .rate(new BigDecimal(rate))
                .date("2018-09-01")
                .build();

        assertThat(rates).isNotEmpty();
        assertThat(rates.get(0)).usingRecursiveComparison()
                .isEqualTo(target);
    }
}