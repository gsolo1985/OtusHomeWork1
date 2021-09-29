package ru.otus.reval.service;

import ru.otus.reval.domain.CurrencyEntity;
import ru.otus.reval.model.CurrencyGet;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {
    /**
     * Получение валюты по id
     * @param id - идентификатор
     * @return - валюта
     */
    Optional<CurrencyEntity> getById(long id);

    /**
     * Получение валюты по имени
     * @param name - имя валюты
     * @return - валюта
     */
    Optional<CurrencyEntity> getByName(String name);

    /**
     * Получение всех валют
     * @return - список валют
     */
    List<CurrencyEntity> findAll();

    /**
     * Удаление валюты
     * @param currencyEntity - валюта
     */
    void delete(CurrencyEntity currencyEntity);

    /**
     * Преобразование entity в dto
     * @param currencyEntity - entity
     * @return - dto
     */
    CurrencyGet transformToDto(CurrencyEntity currencyEntity);

    /**
     * Преобразование dto в entity
     * @param dto - dto
     * @return - entity
     */
    CurrencyEntity transformToEntity(CurrencyGet dto);

    /**
     * Сохранение валюты
     * @param currencyEntity - валюта
     * @return - сохраненный объект
     */
    CurrencyEntity save(CurrencyEntity currencyEntity);
}
