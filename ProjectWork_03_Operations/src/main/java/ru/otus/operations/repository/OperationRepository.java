package ru.otus.operations.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.statemachine.OperationState;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OperationRepository extends CrudRepository<OperationEntity, Long> {
    @Override
    @EntityGraph("operation-entity-graph")
    List<OperationEntity> findAll();

    @EntityGraph("operation-entity-graph")
    List<OperationEntity> findByPlanDateAndState(LocalDate planDate, OperationState state);

    @EntityGraph("operation-entity-graph")
    List<OperationEntity> findByState(OperationState state);

    @EntityGraph("operation-entity-graph")
    List<OperationEntity> findByOperationDate(LocalDate operationDate);
}
