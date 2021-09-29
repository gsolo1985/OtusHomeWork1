package ru.otus.operations.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.SecurityEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityRepository extends CrudRepository<SecurityEntity, Long> {
    @Override
    @EntityGraph("security-entity-graph")
    List<SecurityEntity> findAll();

    @EntityGraph("security-entity-graph")
    Optional<SecurityEntity> findByName(String name);
}
