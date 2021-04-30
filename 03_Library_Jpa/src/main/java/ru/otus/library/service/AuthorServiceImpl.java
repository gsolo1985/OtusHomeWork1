package ru.otus.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;
import ru.otus.library.exception.AuthorDeleteException;
import ru.otus.library.exception.AuthorNotValidException;
import ru.otus.library.repositoriy.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private static final String INSERT_ERROR = "New author insert error: ";
    private static final String DELETE_ERROR = "Delete author error: ";
    private final AuthorRepository authorRepository;
    private final BookService bookService;

    @Override
    public Optional<Author> getById(long id) {
        return authorRepository.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> getByName(String name) {
        return authorRepository.getByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAll() {
        return authorRepository.getAll();
    }

    @Override
    @Transactional
    public Author save(Author author) {
        if (author == null) {
            throw new AuthorNotValidException(INSERT_ERROR + "author isn't defined.");
        }

        String name = author.getName();

        if (name == null || name.equals("")) {
            throw new AuthorNotValidException(INSERT_ERROR + "you must set a name.");
        }

        if (authorRepository.getByName(name).isPresent()) {
            throw new AuthorNotValidException(INSERT_ERROR + "an author with this name already exists.");
        }

        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public void delete(Author author) {
        if (bookService.getByAuthor(author).size() > 0) {
            throw new AuthorDeleteException(DELETE_ERROR + "books by this author exist in the system.");
        }

        authorRepository.delete(author);
    }
}
