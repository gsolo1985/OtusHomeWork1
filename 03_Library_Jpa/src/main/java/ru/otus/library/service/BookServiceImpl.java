package ru.otus.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookComment;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.BookNotValidException;
import ru.otus.library.repositoriy.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final String INSERT_ERROR = "New book insert error: ";
    private static final String UPDATE_ERROR = "Update book error: ";
    private final BookRepository bookRepository;
    private final BookCommentService bookCommentService;

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getById(long id) {
        return bookRepository.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getByTitleAndAuthor(String title, Author author) {
        List<Book> books = bookRepository.getByTitle(title);
        return books.stream()
                .filter(book -> book.getAuthor().getId() == (author.getId()))
                .findAny();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        return bookRepository.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getByAuthor(Author author) {
        if (author != null) {
            return bookRepository.getByAuthor(author);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getByGenre(Genre genre) {
        if (genre != null) {
            return bookRepository.getByGenre(genre);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
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

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(Book book) {
        if (book == null || book.getId() == 0) {
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

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void delete(Book book) {
        List<BookComment> bookComments = bookCommentService.getByBook(book);
        bookComments.forEach(bookCommentService::delete);
        bookRepository.delete(book);
    }
}
