package ru.otus.operations.service;

import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.statemachine.OperationState;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OperationService {
    /**
     * Получить операции по id
     *
     * @param id - идентификатор
     * @return - операция
     */
    Optional<OperationEntity> findById(long id);

    /**
     * Сохранить список операций
     *
     * @param list - список на сохранение
     * @return - сохраненные объекты
     */
    List<OperationEntity> saveAll(List<OperationEntity> list);

    /**
     * Получить список операций по плановой дате и статусу
     *
     * @param planDate - плановая дата
     * @param state    - статус
     * @return - список операций
     */
    List<OperationEntity> findByPlanDateAndState(LocalDate planDate, OperationState state);

    /**
     * Получить список операций статусу
     *
     * @param state - статус
     * @return - список операций
     */
    List<OperationEntity> findByState(OperationState state);
}
