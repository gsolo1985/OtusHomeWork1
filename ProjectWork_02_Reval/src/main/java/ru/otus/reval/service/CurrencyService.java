package ru.otus.reval.service;

import ru.otus.reval.domain.CurrencyEntity;
import ru.otus.reval.model.CurrencyGet;

import java.util.Optional;

public interface CurrencyService {
    Optional<CurrencyEntity> getById(long id);
    Optional<CurrencyEntity> getByName(String name);
    CurrencyGet transformToDto(CurrencyEntity currencyEntity);
    CurrencyEntity transformToEntity(CurrencyGet dto);
    CurrencyEntity save(CurrencyEntity currencyEntity);
}
