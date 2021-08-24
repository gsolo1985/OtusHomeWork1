package ru.otus.operations.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.BusinessProcessByOperDateEntity;
import ru.otus.operations.domain.BusinessProcessEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessProcessByOperDateRepository extends CrudRepository<BusinessProcessByOperDateEntity, Long> {
    Optional<BusinessProcessByOperDateEntity> findByBusinessProcessEntityAndOperDate(BusinessProcessEntity businessProcessEntity, LocalDate operDate);
    List<BusinessProcessByOperDateEntity> findByOperDate(LocalDate operDate);
}
