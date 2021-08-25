package ru.otus.operations.service.accounting;

import ru.otus.operations.domain.DocumentTemplateEntity;
import ru.otus.operations.domain.OperationEntity;

import java.util.List;

public interface DocumentTemplateService {
    /**
     * Получить список подходящих шаблонов документов по сделке
     * @param operationEntity - операция
     * @return - список шаблонов
     */
    List<DocumentTemplateEntity> getDocTemplateForOperation(OperationEntity operationEntity);
}
