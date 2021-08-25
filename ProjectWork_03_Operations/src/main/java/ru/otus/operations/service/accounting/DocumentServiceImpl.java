package ru.otus.operations.service.accounting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.domain.DocumentEntity;
import ru.otus.operations.exception.DocumentException;
import ru.otus.operations.repository.DocumentRepository;

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
        if (documentEntity != null) {
            throw new DocumentException(INSERT_ERROR + " document isn't define.");
        }

        if (documentEntity.getAmount() == null ||
                documentEntity.getOperationEntity() == null) {
            throw new DocumentException(INSERT_ERROR + " incorrect document parameters.");
        }

        return repository.save(documentEntity);
    }
}
