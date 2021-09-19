package ru.otus.operations.service.processing;

import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.domain.OperationEntity;

import java.util.List;

public interface CancelOperationService {
    /**
     * Запуск бизнес-процесса с системным именем "OPERATIONS_CANCEL_SYS_NAME"
     * Отмена операций за дату
     *
     * @param operDateEntity - дата
     * @return - отмененные операции
     */
    List<OperationEntity> exec(OperDateEntity operDateEntity);
}
