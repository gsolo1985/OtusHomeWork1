package ru.otus.operations.service;

import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.publish.operationreval.OperationRevalDto;

import java.util.List;
import java.util.Optional;

public interface OperationService {
    /**
     * Сгенерировать сделки/операции за дату
     * @param operDateEntity - дата
     * @return - сгенерированные сделки/операции
     */
    List<OperationEntity> generateByDate(OperDateEntity operDateEntity);

    /**
     * Отмена операций за плановую дату
     * @param operDateEntity - дата
     * @return - отмененные операции
     */
    List<OperationEntity> cancelByPlanDate(OperDateEntity operDateEntity);

    /**
     * Исполнение операций за плановую дату
     * @param operDateEntity - дата
     * @return - исполненные операции
     */
    List<OperationEntity> execByPlanDate(OperDateEntity operDateEntity);

    /**+
     * Получить все операции, по которым нужно произвести переоценку на дату
     * @param operDateEntity - дата
     * @return - операции для переоценки
     */
    List<OperationRevalDto> getOperationForRevalByDate(OperDateEntity operDateEntity);

    /**
     * Получить операции по id
     * @param id - идентификатор
     * @return - операция
     */
    Optional<OperationEntity> findById(long id);
}
