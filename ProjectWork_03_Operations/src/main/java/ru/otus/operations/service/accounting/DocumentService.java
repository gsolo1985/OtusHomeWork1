package ru.otus.operations.service.accounting;

import ru.otus.operations.domain.DocumentEntity;

public interface DocumentService {
    /**
     * Сохранить документ
     *
     * @param documentEntity - документ
     * @return - сохраненный объект
     */
    DocumentEntity save(DocumentEntity documentEntity);
}
