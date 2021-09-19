package ru.otus.reval.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.reval.domain.CurrencyEntity;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends CrudRepository<CurrencyEntity, Long> {
    Optional<CurrencyEntity> findByName(String name);
}
