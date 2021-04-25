package ru.otus.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.domain.Author;
import ru.otus.library.exception.AuthorDeleteException;
import ru.otus.library.exception.AuthorNotValidException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private static final String INSERT_ERROR = "New author insert error: ";
    private static final String DELETE_ERROR = "Delete author error: ";
    private final AuthorDao authorDao;
    private final BookService bookService;

    @Override
    public Optional<Author> getById(long id) {
        return authorDao.getById(id);
    }

    @Override
    public Optional<Author> getByName(String name) {
        return authorDao.getByName(name);
    }

    @Override
    public List<Author> getAll() {
        return authorDao.getAll();
    }

    @Override
    public Author insert(Author author) {
        if (author == null) {
            throw new AuthorNotValidException(INSERT_ERROR + "author isn't defined.");
        }

        String name = author.getName();

        if (name == null || name.equals("")) {
            throw new AuthorNotValidException(INSERT_ERROR + "you must set a name.");
        }

        if (authorDao.getByName(name).isPresent()) {
            throw new AuthorNotValidException(INSERT_ERROR + "an author with this name already exists.");
        }

        return authorDao.insert(author);
    }

    @Override
    public void delete(Author author) {
        if (bookService.getByAuthor(author).size() > 0) {
            throw new AuthorDeleteException(DELETE_ERROR + "books by this author exist in the system.");
        }

        authorDao.delete(author);
    }
}
