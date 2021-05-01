package ru.otus.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Genre;

import ru.otus.library.exception.GenreDeleteException;
import ru.otus.library.exception.GenreNotValidException;
import ru.otus.library.repositoriy.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private static final String INSERT_ERROR = "New genre insert error: ";
    private static final String DELETE_ERROR = "Delete genre error: ";
    private final GenreRepository genreRepository;
    private final BookService bookService;

    @Override
    public Optional<Genre> getById(long id) {
        return genreRepository.getById(id);
    }

    @Override
    public Optional<Genre> getByName(String name) {
        return genreRepository.getByName(name);
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.getAll();
    }

    @Override
    @Transactional
    public Genre save(Genre genre) {
        if (genre == null) {
            throw new GenreNotValidException(INSERT_ERROR + "genre isn't define.");
        }

        String name = genre.getName();

        if (name == null || name.equals("")) {
            throw new GenreNotValidException(INSERT_ERROR + "you must set a name.");
        }

        if (genreRepository.getByName(name).isPresent()) {
            throw new GenreNotValidException(INSERT_ERROR + "a genre with this name already exists.");
        }

        return genreRepository.save(genre);
    }

    @Override
    @Transactional
    public void delete(Genre genre) {
        if (bookService.getByGenre(genre).size() > 0) {
            throw new GenreDeleteException(DELETE_ERROR + "this genre's books exist in the system.");
        }
        genreRepository.delete(genre);
    }
}
