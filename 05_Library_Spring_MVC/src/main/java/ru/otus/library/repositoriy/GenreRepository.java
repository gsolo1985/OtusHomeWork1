package ru.otus.library.repositoriy;

import org.springframework.data.repository.CrudRepository;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    Optional<Genre> findByName(String name);

    @Override
    List<Genre> findAll();

    boolean existsByName(String name);
}
