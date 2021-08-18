package ru.otus.operations.service;

import ru.otus.operations.domain.OperDateEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface OperDateService {
    /**
     * Заполнить БД операционными датами
     */
    void fillOperDate();

    /**
     * Открывает новый операционный день
     */
    OperDateEntity openOperDay();

    /**
     * Получить текущий операционный день
     */
    Optional<OperDateEntity> getOperDay();

    /**
     * Закрыть операционный день
     */
    void closeOperDay(LocalDate date);
    void closeOperDay(OperDateEntity operDateEntity);

}
