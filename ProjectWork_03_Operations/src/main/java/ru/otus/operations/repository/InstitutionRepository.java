package ru.otus.operations.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.InstitutionEntity;

@Repository
public interface InstitutionRepository extends CrudRepository<InstitutionEntity, Long> {
}
