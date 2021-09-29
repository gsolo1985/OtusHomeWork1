package ru.otus.operations.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.constants.DocType;
import ru.otus.operations.domain.DocumentEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentRepository extends CrudRepository<DocumentEntity, Long> {
    @EntityGraph("document-entity-graph")
    List<DocumentEntity> findByOperDateAndDocType(LocalDate operDate, DocType docType);
}
