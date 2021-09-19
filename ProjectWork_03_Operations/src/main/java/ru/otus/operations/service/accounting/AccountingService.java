package ru.otus.operations.service.accounting;

import ru.otus.operations.domain.DocumentEntity;
import ru.otus.operations.domain.OperationEntity;

import java.time.LocalDate;
import java.util.List;

public interface AccountingService {
    /**
     * Выполнить учет по операции на дату
     *
     * @param operationEntity - операция
     * @param operDate - дата
     * @return - список документов
     */
    List<DocumentEntity> execAccountingByOperationOnDate(OperationEntity operationEntity, LocalDate operDate);
}
