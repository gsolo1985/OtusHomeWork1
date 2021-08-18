package ru.otus.reval.service;

import ru.otus.reval.consumer.CurrencyRateDto;
import ru.otus.reval.domain.CurrencyRateEntity;

import java.util.List;

public interface RateTransformService {
    void setCurrencyEntityToListRateDto(List<CurrencyRateDto> rates);
    List<CurrencyRateEntity> getCurrencyRateEntityFromRateDto(List<CurrencyRateDto> rates);
}
