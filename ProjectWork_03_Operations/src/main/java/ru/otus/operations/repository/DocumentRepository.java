package ru.otus.operations.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.DocumentEntity;

@Repository
public interface DocumentRepository extends CrudRepository<DocumentEntity, Long> {
}
