package ru.otus.currencyAdapter.service;

import ru.otus.currencyAdapter.publish.CurrencyRateDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface RateTransformService {
    List<CurrencyRateDto> layerTransformToDto(Map<String, Object> layer, int parseMode) throws IOException;
}
