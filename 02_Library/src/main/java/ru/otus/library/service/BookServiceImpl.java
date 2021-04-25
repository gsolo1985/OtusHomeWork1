package ru.otus.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.BookDao;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.BookNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final String INSERT_ERROR = "New book insert error: ";
    private static final String UPDATE_ERROR = "Update book error: ";
    private final BookDao bookDao;

    @Override
    public Optional<Book> getById(long id) {
        return bookDao.getById(id);
    }

    @Override
    public Optional<Book> getByTitleAndAuthor(String title, Author author) {
        if (author == null) {
            return Optional.empty();
        }
        return bookDao.getByTitleAndAuthor(title, author);
    }

    @Override
    public List<Book> getAll() {
        return bookDao.getAll();
    }

    @Override
    public List<Book> getByAuthor(Author author) {
        if (author != null) {
            return bookDao.getByAuthor(author);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Book> getByGenre(Genre genre) {
        if (genre != null) {
            return bookDao.getByGenre(genre);
        }
        return new ArrayList<>();
    }

    @Override
    public Book insert(Book book) {
        if (book == null) {
            throw new BookNotValidException(INSERT_ERROR + "book isn't define.");
        }

        if (book.getAuthor() == null) {
            throw new BookNotValidException(INSERT_ERROR + "you must set a book's author.");
        }

        if (book.getGenre() == null) {
            throw new BookNotValidException(INSERT_ERROR + "you must set a book's genre.");
        }

        String title = book.getTitle();

        if (title == null || title.equals("")) {
            throw new BookNotValidException(INSERT_ERROR + "you must set a book's title.");
        }

        if (getByTitleAndAuthor(title, book.getAuthor()).isPresent()) {
            throw new BookNotValidException(INSERT_ERROR + "a book with this title and author already exists.");
        }

        return bookDao.insert(book);
    }


    @Override
    public Book update(Book book) {
        if (book == null) {
            throw new BookNotValidException(UPDATE_ERROR + "book isn't define.");
        }

        if (book.getAuthor() == null) {
            throw new BookNotValidException(UPDATE_ERROR + "you must set a book's author.");
        }

        if (book.getGenre() == null) {
            throw new BookNotValidException(UPDATE_ERROR + "you must set a book's genre.");
        }

        String title = book.getTitle();

        if (title == null || title.equals("")) {
            throw new BookNotValidException(UPDATE_ERROR + "you must set a book's title.");
        }

        long currentId = book.getId();
        Optional<Book> bookOptionalByTitle = getByTitleAndAuthor(title, book.getAuthor());

        if (bookOptionalByTitle.isPresent() && bookOptionalByTitle.get().getId() != currentId) {
            throw new BookNotValidException(UPDATE_ERROR + "a book with this title and author already exists.");
        }

        return bookDao.update(book);
    }

    @Override
    public void delete(Book book) {
        bookDao.delete(book);
    }
}
