package ru.otus.operations.service;

import ru.otus.operations.model.OperationDto;

public interface OperationSaveService {
    /**
     * Сохранить операцию через dto-объект
     *
     * @param operationDto - dto
     * @return - сохраненный объект
     */
    OperationDto saveDto(OperationDto operationDto);
}
