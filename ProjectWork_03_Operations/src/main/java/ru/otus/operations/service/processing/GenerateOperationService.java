package ru.otus.operations.service.processing;

import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.domain.OperationEntity;

import java.util.List;

public interface GenerateOperationService {
    /**
     * Запуск бизнес-процесса с системным именем "OPERATIONS_CREATE_SYS_NAME"
     * Сгенерировать сделки/операции за дату
     *
     * @param operDateEntity - дата
     * @return - сгенерированные сделки/операции
     */
    List<OperationEntity> exec(OperDateEntity operDateEntity);
}
