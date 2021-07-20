package ru.otus.library.service;

import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Optional<Genre> getById(long id);

    Optional<Genre> getByName(String name);

    List<Genre> getAll();

    Genre save (Genre genre);

    void delete (Genre genre);
}
