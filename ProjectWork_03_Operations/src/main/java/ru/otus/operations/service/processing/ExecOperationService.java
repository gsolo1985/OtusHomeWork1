package ru.otus.operations.service.processing;

import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.domain.OperationEntity;

import java.util.List;

public interface ExecOperationService {
    /**
     * Запуск бизнес-процесса с системным именем "OPERATIONS_EXECUTION_SYS_NAME"
     * Исполнение операций за плановую дату
     *
     * @param operDateEntity - дата
     * @return - исполненные операции
     */
    List<OperationEntity> exec(OperDateEntity operDateEntity);

}
