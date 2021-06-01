package ru.otus.library.repositoriy;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.otus.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Optional<Author> findByName(String name);

    @Override
    List<Author> findAll();

    boolean existsByName(String name);
}
