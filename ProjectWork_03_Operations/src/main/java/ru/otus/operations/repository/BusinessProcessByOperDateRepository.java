package ru.otus.operations.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.BusinessProcessByOperDateEntity;

@Repository
public interface BusinessProcessByOperDateRepository extends CrudRepository<BusinessProcessByOperDateEntity, Long> {
}
