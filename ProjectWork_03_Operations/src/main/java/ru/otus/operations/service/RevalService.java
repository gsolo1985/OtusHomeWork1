package ru.otus.operations.service;

import ru.otus.operations.consumer.RevalOperation;
import ru.otus.operations.domain.RevalEntity;

public interface RevalService {
    /**
     * Сохранение переоценки по операции
     * @param reval - объект с переоценкой
     * @return - сохраненный объект
     */
    RevalEntity save(RevalEntity reval);

    /**
     * Преобразование dto-объкта в Entity-объект
     * @param dto - dto-объкт
     * @return - Entity-объект
     */
    RevalEntity dtoTransformToEntity(RevalOperation dto);
}
