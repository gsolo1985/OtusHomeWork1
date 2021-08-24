package ru.otus.operations.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.domain.RevalEntity;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RevalRepository extends CrudRepository<RevalEntity, Long> {
    Optional<RevalEntity> findByOperationAndOperDate(OperationEntity operationEntity, LocalDate operDate);
}
