package ru.otus.operations.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.OperDateEntity;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OperDateRepository extends CrudRepository<OperDateEntity, Long> {
    Optional<OperDateEntity> findTop1ByStatusOrderByOperDate(int status);
    Optional<OperDateEntity> findTop1ByStatusOrderByOperDateDesc(int status);
    Optional<OperDateEntity> findTop1ByOperDateGreaterThanOrderByOperDate(LocalDate operDate);
    Optional<OperDateEntity> findByOperDate(LocalDate operDate);
    boolean existsByOperDateIdGreaterThan(long id);
}
