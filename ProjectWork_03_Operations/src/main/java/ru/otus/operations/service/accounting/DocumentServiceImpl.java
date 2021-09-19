package ru.otus.operations.service.accounting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.constants.DocType;
import ru.otus.operations.domain.DocumentEntity;
import ru.otus.operations.exception.DocumentException;
import ru.otus.operations.model.DocumentDto;
import ru.otus.operations.repository.DocumentRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository repository;
    private static final String INSERT_ERROR = "New document insert error: ";

    /**
     * Сохранить документ
     *
     * @param documentEntity - документ
     * @return - сохраненный объект
     */
    @Override
    @Transactional
    public DocumentEntity save(DocumentEntity documentEntity) {
        if (documentEntity == null) {
            throw new DocumentException(INSERT_ERROR + " document isn't define.");
        }

        if (documentEntity.getAmount() == null ||
                documentEntity.getOperationEntity() == null) {
            throw new DocumentException(INSERT_ERROR + " incorrect document parameters.");
        }

        return repository.save(documentEntity);
    }

    /**
     * Получить документы по дате и типу документа
     *
     * @param operDate - дата
     * @param docType  - тип документа
     * @return - список документов
     */
    @Override
    @Transactional(readOnly = true)
    public List<DocumentEntity> findByOperDateAndDocType(LocalDate operDate, DocType docType) {
        return repository.findByOperDateAndDocType(operDate, docType);
    }

    /**
     * Преобразование объекта entity в объект dto
     *
     * @param entity - entity-объект
     * @return - Dto-объект
     */
    @Override
    public DocumentDto entityToDto(DocumentEntity entity) {
        return DocumentDto.builder()
                .documentId(entity.getDocumentId())
                .amount(entity.getAmount())
                .comment(entity.getComment())
                .creditAccountNumber(entity.getCreditAccountNumber())
                .debitAccountNumber(entity.getDebitAccountNumber())
                .operationId(entity.getOperationEntity().getOperationId())
                .operDate(entity.getOperDate())
                .docTypeName(entity.getDocType().getName())
                .build();
    }
}
