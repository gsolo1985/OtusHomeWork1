package ru.otus.library.service;

import ru.otus.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Optional<Author> getById(long id);

    Optional<Author> getByName(String name);

    List<Author> getAll();

    Author insert (Author author);

    void delete (Author author);
}
