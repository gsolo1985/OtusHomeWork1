package ru.otus.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.GenreDeleteException;
import ru.otus.library.exception.GenreNotValidException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private static final String INSERT_ERROR = "New genre insert error: ";
    private static final String DELETE_ERROR = "Delete genre error: ";
    private final GenreDao genreDao;
    private final BookService bookService;

    @Override
    public Optional<Genre> getById(long id) {
        return genreDao.getById(id);
    }

    @Override
    public Optional<Genre> getByName(String name) {
        return genreDao.getByName(name);
    }

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    @Override
    public Genre insert(Genre genre) {
        if (genre == null) {
            throw new GenreNotValidException(INSERT_ERROR + "genre isn't define.");
        }

        String name = genre.getName();

        if (name == null || name.equals("")) {
            throw new GenreNotValidException(INSERT_ERROR + "you must set a name.");
        }

        if (genreDao.getByName(name).isPresent()) {
            throw new GenreNotValidException(INSERT_ERROR + "a genre with this name already exists.");
        }

        return genreDao.insert(genre);
    }

    @Override
    public void delete(Genre genre) {
        if (bookService.getByGenre(genre).size() > 0) {
            throw new GenreDeleteException(DELETE_ERROR + "this genre's books exist in the system.");
        }
        genreDao.delete(genre);
    }
}
