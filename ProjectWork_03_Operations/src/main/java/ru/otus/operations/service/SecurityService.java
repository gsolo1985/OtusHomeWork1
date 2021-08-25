package ru.otus.operations.service;

import ru.otus.operations.domain.SecurityEntity;

public interface SecurityService {
    /**
     * Получить случайную ЦБ из БД
     *
     * @return - ЦБ
     */
    SecurityEntity getRandomSecurity();
}
