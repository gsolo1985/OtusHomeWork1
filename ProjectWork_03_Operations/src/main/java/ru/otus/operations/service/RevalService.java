package ru.otus.operations.service;

import ru.otus.operations.consumer.RevalOperation;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.domain.RevalEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface RevalService {
    /**
     * Сохранение переоценки по операции
     *
     * @param reval - объект с переоценкой
     * @return - сохраненный объект
     */
    RevalEntity save(RevalEntity reval);

    /**
     * Преобразование dto-объкта в Entity-объект
     *
     * @param dto - dto-объкт
     * @return - Entity-объект
     */
    RevalEntity dtoTransformToEntity(RevalOperation dto);

    /**
     * Получить переоценку по операции на дату
     *
     * @param operationEntity - операция
     * @param localDate       - дата
     * @return - переоценка
     */
    Optional<RevalEntity> getRevalByOperationAndDate(OperationEntity operationEntity, LocalDate localDate);
}
