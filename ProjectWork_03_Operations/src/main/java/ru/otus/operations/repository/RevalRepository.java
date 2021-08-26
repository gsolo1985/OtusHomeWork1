package ru.otus.operations.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.domain.RevalEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RevalRepository extends CrudRepository<RevalEntity, Long> {
    @EntityGraph("reval-entity-graph")
    Optional<RevalEntity> findByOperationEntityAndOperDate(OperationEntity operationEntity, LocalDate operDate);

    @EntityGraph("reval-entity-graph")
    List<RevalEntity> findByOperDate(LocalDate operDate);

    @EntityGraph("reval-entity-graph")
    List<RevalEntity> findByOperationEntity(OperationEntity operationEntity);

}
