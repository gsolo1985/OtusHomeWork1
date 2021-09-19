package ru.otus.operations.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.ProtocolEntity;
import ru.otus.operations.domain.BusinessProcessEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProtocolRepository extends CrudRepository<ProtocolEntity, Long> {
    Optional<ProtocolEntity> findByBusinessProcessEntityAndOperDate(BusinessProcessEntity businessProcessEntity, LocalDate operDate);
    List<ProtocolEntity> findByOperDate(LocalDate operDate);
}
