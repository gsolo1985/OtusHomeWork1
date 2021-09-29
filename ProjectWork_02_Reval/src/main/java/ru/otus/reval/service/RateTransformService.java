package ru.otus.reval.service;

import ru.otus.reval.consumer.currencyRate.CurrencyRateDto;
import ru.otus.reval.domain.CurrencyRateEntity;

import java.util.List;

public interface RateTransformService {
    /**
     * Установить валюту из БД в списке курсов/котировок
     * @param rates - dto список котировок
     */
    void setCurrencyEntityToListRateDto(List<CurrencyRateDto> rates);

    /**
     * Преобразование объектов-dto в объекты-entity
     * @param rates - объекты-dto
     * @return - объекты-entity
     */
    List<CurrencyRateEntity> getCurrencyRateEntityFromRateDto(List<CurrencyRateDto> rates);
}
