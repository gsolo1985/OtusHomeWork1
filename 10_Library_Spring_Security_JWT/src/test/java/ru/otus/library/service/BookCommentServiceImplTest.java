package ru.otus.library.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookComment;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.BookCommentNotValidException;
import ru.otus.library.repositoriy.BookCommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование BookCommentServiceImpl")
class BookCommentServiceImplTest {
    private static final String TOLSTOY_AUTHOR_NAME = "Lev Tolstoy";
    private static final long TOLSTOY_AUTHOR_MOCK_ID = 10L;

    private static final String DRAMA_GENRE_NAME = "Drama";
    private static final long DRAMA_GENRE_MOCK_ID = 10L;

    private static final String WAR_AND_PEACE_BOOK_TITLE = "War and Peace";
    private static final long WAR_AND_PEACE_BOOK_ID = 10L;

    private static final int EXPECTED_NUMBER_OF_COMMENTS = 2;
    private static final long GOOD_COMMENT_MOCK_ID = 10L;
    private static final long BAD_COMMENT_MOCK_ID = 11L;
    private static final String GOOD_COMMENT_VALUE = "Good comment";
    private static final String BAD_COMMENT_VALUE = "Good comment";

    private BookCommentService bookCommentService;

    @Mock
    private BookCommentRepository bookCommentRepository;

    private Book warAndPeaceBook;
    private BookComment goodComment;
    private BookComment badComment;

    @BeforeAll
    public void setUp() {
        warAndPeaceBook = Book.builder()
                .id(WAR_AND_PEACE_BOOK_ID)
                .title(WAR_AND_PEACE_BOOK_TITLE)
                .author(Author.builder().id(TOLSTOY_AUTHOR_MOCK_ID).name(TOLSTOY_AUTHOR_NAME).build())
                .genre(Genre.builder().id(DRAMA_GENRE_MOCK_ID).name(DRAMA_GENRE_NAME).build())
                .build();

        goodComment = BookComment.builder().id(GOOD_COMMENT_MOCK_ID).book(warAndPeaceBook).comment(GOOD_COMMENT_VALUE).build();
        badComment = BookComment.builder().id(BAD_COMMENT_MOCK_ID).book(warAndPeaceBook).comment(BAD_COMMENT_VALUE).build();
    }

    @BeforeEach
    public void init() {
        bookCommentService = new BookCommentServiceImpl(bookCommentRepository);
    }

    @DisplayName("Возвращать ошибку при добавлении если нет объекта книги")
    @Test
    void save() {
        BookComment newComment = new BookComment(0, null, "");

        Throwable thrown = assertThrows(BookCommentNotValidException.class, () -> {
            bookCommentService.save(newComment);
        });

        assertThat(thrown.getMessage()).isNotNull();
    }

    @DisplayName("Возвращать комментарий по id")
    @Test
    void shouldGetById() {
        Mockito.when(bookCommentRepository.findById(GOOD_COMMENT_MOCK_ID)).thenReturn(Optional.ofNullable(goodComment));
        BookComment comment = bookCommentService.getById(GOOD_COMMENT_MOCK_ID).get();

        assertThat(comment.getComment()).isEqualTo(GOOD_COMMENT_VALUE);
    }
}