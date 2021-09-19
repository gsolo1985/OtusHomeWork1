package ru.otus.operations.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.DocumentTemplateEntity;

@Repository
public interface DocumentTemplateRepository extends CrudRepository<DocumentTemplateEntity, Long> {
}
