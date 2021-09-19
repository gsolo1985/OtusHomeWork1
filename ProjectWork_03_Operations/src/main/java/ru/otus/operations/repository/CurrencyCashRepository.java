package ru.otus.operations.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.CurrencyCashEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyCashRepository extends CrudRepository<CurrencyCashEntity, Long> {
    @Override
    List<CurrencyCashEntity> findAll();

    Optional<CurrencyCashEntity> findByName(String name);
}
