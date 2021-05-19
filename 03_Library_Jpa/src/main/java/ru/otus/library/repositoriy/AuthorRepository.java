package ru.otus.library.repositoriy;

import ru.otus.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> getById(long id);

    Optional<Author> getByName(String name);

    List<Author> getAll();

    Author save (Author author);

    void delete (Author author);
}
