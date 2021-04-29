package ru.otus.library.dao;

import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Optional<Genre> getById(long id);

    Optional<Genre> getByName(String name);

    List<Genre> getAll();

    Genre insert (Genre genre);

    void delete (Genre genre);
}
