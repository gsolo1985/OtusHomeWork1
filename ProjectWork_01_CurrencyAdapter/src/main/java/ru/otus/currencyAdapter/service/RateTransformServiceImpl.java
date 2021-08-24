package ru.otus.currencyAdapter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.currencyAdapter.publish.CurrencyRateDto;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RateTransformServiceImpl implements RateTransformService {
    /**
     *Преобразование объектов из источника данных в объект для отправку в кафку
     */
    @Override
    public List<CurrencyRateDto> layerTransformToDto(Map<String, Object> layer, int parseMode) throws IOException {
        List<CurrencyRateDto> result = new ArrayList<>();

        if (layer.get("quotes") != null) {
            StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            LocalDate date = LocalDate.now();

            mapper.writeValue(writer, layer.get("quotes"));

            var ratesAll = writer.toString().substring(1, writer.toString().length() - 1).replace("\"", "").split(",");

            if (layer.get("date") != null) {
                date = LocalDate.parse(layer.get("date").toString());
            }

            for (String rateStr : ratesAll) {
                var rate = rateStr.split(":");

                result.add(CurrencyRateDto.builder()
                        .currencyPair((parseMode == 0) ? rate[0] : rate[0].substring(2, 8))
                        .rate((parseMode == 0) ? new BigDecimal(rate[1]) : new BigDecimal(rate[1].substring(0, rate[1].length() - 1)))
                        .date(date.toString())
                        .build());
            }
        }
        return result;
    }
}

