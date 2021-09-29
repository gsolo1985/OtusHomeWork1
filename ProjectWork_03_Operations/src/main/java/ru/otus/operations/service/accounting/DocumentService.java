package ru.otus.operations.service.accounting;

import ru.otus.operations.constants.DocType;
import ru.otus.operations.domain.DocumentEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.model.DocumentDto;
import ru.otus.operations.model.OperationDto;

import java.time.LocalDate;
import java.util.List;

public interface DocumentService {
    /**
     * Сохранить документ
     *
     * @param documentEntity - документ
     * @return - сохраненный объект
     */
    DocumentEntity save(DocumentEntity documentEntity);

    /**
     * Получить документы по дате и типу документа
     *
     * @param operDate - дата
     * @param docType  - тип документа
     * @return - список документов
     */
    List<DocumentEntity> findByOperDateAndDocType(LocalDate operDate, DocType docType);

    /**
     * Преобразование объекта entity в объект dto
     *
     * @param entity - entity-объект
     * @return - Dto-объект
     */
    DocumentDto entityToDto(DocumentEntity entity);
}
