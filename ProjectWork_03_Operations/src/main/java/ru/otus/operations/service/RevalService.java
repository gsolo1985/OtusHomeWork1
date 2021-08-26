package ru.otus.operations.service;

import ru.otus.operations.consumer.RevalOperation;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.domain.RevalEntity;
import ru.otus.operations.model.RevalDto;

import java.time.LocalDate;
import java.util.List;
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

    /**
     * Получить переоценку за дату
     *
     * @param operDate - дата
     * @return - переоценка
     */
    List<RevalEntity> findByOperDate(LocalDate operDate);

    /**
     * Преобразование объекта entity в объект dto
     *
     * @param entity - entity-объект
     * @return - Dto-объект
     */
    RevalDto entityToDto(RevalEntity entity);

    /**
     * Получить историю переоценку по операции
     *
     * @param operationEntity - операция
     * @return список расчитанных переоценнок
     */
    List<RevalEntity> findByOperationEntity(OperationEntity operationEntity);
}
