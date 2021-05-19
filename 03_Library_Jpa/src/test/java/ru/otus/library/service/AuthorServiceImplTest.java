package ru.otus.library.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.library.domain.Author;
import ru.otus.library.exception.AuthorDeleteException;
import ru.otus.library.exception.AuthorNotValidException;
import ru.otus.library.repositoriy.AuthorRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование AuthorServiceImpl")
class AuthorServiceImplTest {
    private static final String TOLSTOY_AUTHOR_NAME = "Lev Tolstoy";
    private static final String CHEKHOV_AUTHOR_NAME = "Anton Chekhov";
    private static final long TOLSTOY_AUTHOR_MOCK_ID = 10L;
    private static final long CHEKHOV_AUTHOR_MOCK_ID = 11L;
    private static final int EXPECTED_NUMBER_OF_AUTHORS = 2;

    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookService bookService;

    private Author tolstoyAuthor;
    private Author chekhovAuthor;

    @BeforeAll
    public void setUp() {
        tolstoyAuthor = new Author(TOLSTOY_AUTHOR_MOCK_ID, TOLSTOY_AUTHOR_NAME);
        chekhovAuthor = new Author(CHEKHOV_AUTHOR_MOCK_ID, CHEKHOV_AUTHOR_NAME);

        Mockito.when(authorRepository.getById(TOLSTOY_AUTHOR_MOCK_ID)).thenReturn(Optional.ofNullable(tolstoyAuthor));
        Mockito.when(authorRepository.getByName(CHEKHOV_AUTHOR_NAME)).thenReturn(Optional.ofNullable(chekhovAuthor));
        Mockito.when(authorRepository.getAll()).thenReturn(Arrays.asList(tolstoyAuthor, chekhovAuthor));
        Mockito.when(bookService.getByAuthor(any())).thenReturn(Collections.singletonList(any()));
    }

    @BeforeEach
    public void init() {
        authorService = new AuthorServiceImpl(authorRepository, bookService);
    }

    @DisplayName("Возвращать автора по id")
    @Test
    void shouldGetById() {
        Author author = authorService.getById(TOLSTOY_AUTHOR_MOCK_ID).get();

        assertThat(author.getName()).isEqualTo(TOLSTOY_AUTHOR_NAME);
    }

    @DisplayName("Возвращать всех авторов")
    @Test
    void getAll() {
        List<Author> actualAuthorList = authorService.getAll();
        assertThat(actualAuthorList).hasSize(EXPECTED_NUMBER_OF_AUTHORS).containsExactly(tolstoyAuthor, chekhovAuthor);
    }

    @DisplayName("Возвращать ошибку при добавлении, если нет имени")
    @Test
    void save() {
        Author newAuthor = new Author(0, "");

        Throwable thrown = assertThrows(AuthorNotValidException.class, () -> {
            authorService.save(newAuthor);
        });

        assertThat(thrown.getMessage()).isNotNull();
    }

    @DisplayName("Возвращать ошибку при удалении если книги с этим автором")
    @Test
    void delete() {
        Throwable thrown = assertThrows(AuthorDeleteException.class, () -> {
            authorService.delete(tolstoyAuthor);
        });

        assertThat(thrown.getMessage()).isNotNull();
    }
}