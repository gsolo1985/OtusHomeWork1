package ru.otus.reval.service;

import ru.otus.reval.domain.CurrencyRateEntity;
import ru.otus.reval.model.CurrencyRateGet;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRateService {
    /**
     * Сохранить сущность котировка
     * @param currencyRateEntity - entity-объект котировка
     * @return - сохраненный объект
     */
    CurrencyRateEntity save(CurrencyRateEntity currencyRateEntity);

    /**
     * Сохранить котировку из dto-объекта
     * @param currencyRateGet - Dto
     * @return - сохраненный объект
     */
    CurrencyRateEntity saveDto(CurrencyRateGet currencyRateGet);

    /**
     * Получить dto-объект котировки по id
     * @param id - идентификатор котировки
     * @return - котировка
     */
    CurrencyRateGet getById(long id);

    /**
     * Получить курс по валютной паре за дату
     * @param currencyPair - валютная пара в формате "USDRUB"
     * @param date - дата
     * @return - котировка
     */
    CurrencyRateGet getByCurrencyPairAndDate(String currencyPair, LocalDate date);

    /**
     * Преобразование entity в dto
     * @param currencyRateEntity - entity
     * @return - dto
     */
    CurrencyRateGet transformToDto(CurrencyRateEntity currencyRateEntity);

    /**
     * Удаление по id
     * @param id - идентификатор
     */
    void deleteById (long id);
}
