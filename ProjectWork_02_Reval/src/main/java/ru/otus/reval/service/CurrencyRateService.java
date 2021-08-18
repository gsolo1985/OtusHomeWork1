package ru.otus.reval.service;

import ru.otus.reval.domain.CurrencyRateEntity;
import ru.otus.reval.model.CurrencyRateGet;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRateService {
    CurrencyRateEntity save(CurrencyRateEntity currencyRateEntity);
    CurrencyRateEntity saveDto(CurrencyRateGet currencyRateGet);
    CurrencyRateGet getById(long id);
    CurrencyRateGet getByCurrencyPairAndDate(String currencyPair, LocalDate date);
    CurrencyRateGet transformToDto(CurrencyRateEntity currencyRateEntity);
}
