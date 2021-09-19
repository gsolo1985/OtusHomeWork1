package ru.otus.operations.service;

import ru.otus.operations.domain.SecurityEntity;

import java.util.Optional;

public interface SecurityService {
    /**
     * Получить случайную ЦБ из БД
     *
     * @return - ЦБ
     */
    SecurityEntity getRandomSecurity();

    /**
     * Получить ЦБ по имени
     *
     * @param name - имя
     * @return ЦБ
     */
    Optional<SecurityEntity> getByName(String name);
}
