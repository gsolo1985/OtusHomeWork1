package ru.otus.operations.service;

import ru.otus.operations.domain.CurrencyCashEntity;

import java.util.Optional;

public interface CurrencyCashService {
    /** Получить рандомную валюту из БД
     *
     * @return валюта
     */
    CurrencyCashEntity getRandomCurrency();

    /**
     * Получить валюту по имени (Если такой валюты нет, то она будет добавлена)
     * @param name - имя
     * @return - валюта
     */
    CurrencyCashEntity getByName(String name);

    /**
     * Сохранить валюту
     * @param currency - валюта
     * @return - сохраненный объект
     */
    CurrencyCashEntity save(CurrencyCashEntity currency);
}
