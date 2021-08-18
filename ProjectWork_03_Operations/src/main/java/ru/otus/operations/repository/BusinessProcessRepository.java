package ru.otus.operations.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.operations.domain.BusinessProcessEntity;

import java.util.List;

@Repository
public interface BusinessProcessRepository extends CrudRepository<BusinessProcessEntity, Long> {
    List<BusinessProcessEntity> findAll(Sort sort);
}
