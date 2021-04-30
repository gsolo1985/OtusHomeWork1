package ru.otus.library.repositoriy;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookComment;
import ru.otus.library.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тест репозитория на основе Jpa для работы со комментариями книг BookCommentRepositoryJpa")
@DataJpaTest
@Import(BookCommentRepositoryJpa.class)
class BookCommentRepositoryJpaTest {

    private static final int EXPECTED_NUMBER_OF_WAR_AND_PEACE_COMMENTS = 2;
    private static final long FIRST_COMMENT_BOOK_ID = 1L;
    private static final long FIRST_BOOK_ID = 1L;

    private static final String NEW_BOOK_NAME = "New Book";
    private static final String NEW_AUTHOR_NAME = "New Author";
    private static final String NEW_GENRE_NAME = "New Genre";
    private static final String NEW_COMMENT = "New comment";

    private static final String TOLSTOY_AUTHOR_NAME = "Lev Tolstoy";
    private static final long TOLSTOY_AUTHOR_ID = 2L;
    private static final String DRAMA_GENRE_NAME = "Drama";
    private static final long DRAMA_GENRE_ID = 1L;
    private static final String WAR_AND_PEACE_BOOK_TITLE = "War and Peace";

    private static final long FIRST_COMMENT_ID = 1L;

    @Autowired
    private BookCommentRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращать комментарий по id")
    @Test
    void shouldReturnExpectedCommentById() {
        val optionalActualComment = repositoryJpa.getById(FIRST_COMMENT_ID);
        val expectedComment = em.find(BookComment.class, FIRST_COMMENT_ID);
        assertThat(optionalActualComment).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("Сохранять комментарий о книге")
    @Test
    void shouldSave() {
        val author = Author.builder().id(0).name(NEW_AUTHOR_NAME).build();
        val genre = Genre.builder().id(0).name(NEW_GENRE_NAME).build();
        val book = Book.builder().id(0).title(NEW_BOOK_NAME).author(author).genre(genre).build();
        val newComment = BookComment.builder().id(0).book(book).comment(NEW_COMMENT).build();

        repositoryJpa.save(newComment);
        assertThat(newComment.getId()).isGreaterThan(0);

        val actualComment = em.find(BookComment.class, newComment.getId());

        assertThat(actualComment).isNotNull().matches(b -> b.getComment().equals(NEW_COMMENT))
                .matches(b -> b.getBook() != null && b.getBook().getId() > 0 && b.getBook().getTitle().equals(NEW_BOOK_NAME));
    }

    @DisplayName("Удалять комментарий по книге")
    @Test
    void shouldDelete() {
        val firstComment = em.find(BookComment.class, FIRST_COMMENT_BOOK_ID);
        assertThat(firstComment).isNotNull();

        repositoryJpa.delete(firstComment);
        val deletedComment = em.find(BookComment.class, FIRST_COMMENT_BOOK_ID);

        assertThat(deletedComment).isNull();
    }

    @DisplayName("Получать комментарии по книге")
    @Test
    void shouldGetByBook() {
        val book = Book.builder().id(FIRST_BOOK_ID)
                .author(Author.builder().id(TOLSTOY_AUTHOR_ID).name(TOLSTOY_AUTHOR_NAME).build())
                .genre(Genre.builder().id(DRAMA_GENRE_ID).name(DRAMA_GENRE_NAME).build())
                .title(WAR_AND_PEACE_BOOK_TITLE)
                .build();

        val comments = repositoryJpa.getByBook(book);

        assertThat(comments).isNotNull().hasSize(EXPECTED_NUMBER_OF_WAR_AND_PEACE_COMMENTS)
                .allMatch(bookComment -> bookComment.getBook() != null && bookComment.getBook().getTitle().equals(WAR_AND_PEACE_BOOK_TITLE));
    }
}