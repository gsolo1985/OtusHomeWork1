package ru.otus.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.BookComment;
import ru.otus.library.exception.BookCommentNotValidException;
import ru.otus.library.repositoriy.BookCommentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {
    private static final String INSERT_ERROR = "New comment insert error: ";
    private final BookCommentRepository bookCommentRepository;

    @Override
    @Transactional
    public BookComment save(BookComment bookComment) {
        if (bookComment == null) {
            throw new BookCommentNotValidException(INSERT_ERROR + "book comment isn't define.");
        }

        if (bookComment.getBook() == null) {
            throw new BookCommentNotValidException(INSERT_ERROR + "you must set a book.");
        }

        String comment = bookComment.getComment();

        if (comment == null || comment.equals("")) {
            throw new BookCommentNotValidException(INSERT_ERROR + "you must set a comment.");
        }

        return bookCommentRepository.save(bookComment);
    }

    @Override
    @Transactional
    public void delete(BookComment bookComment) {
        bookCommentRepository.delete(bookComment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookComment> getById(long id) {
        return bookCommentRepository.findById(id);
    }
}
