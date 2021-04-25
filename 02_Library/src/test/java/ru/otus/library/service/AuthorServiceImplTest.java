package ru.otus.library.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.domain.Author;
import ru.otus.library.exception.AuthorDeleteException;
import ru.otus.library.exception.AuthorNotValidException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование AuthorServiceImpl")
class AuthorServiceImplTest {
    private AuthorService authorService;

    @Mock
    AuthorDao authorDao;
    @Mock
    BookService bookService;

    private Author tolstoyAuthor;
    private Author chekhovAuthor;

    @BeforeAll
    public void setUp() {
        tolstoyAuthor = new Author(10, "Lev Tolstoy");
        chekhovAuthor = new Author(11, "Anton Chekhov");

        Mockito.when(authorDao.getById(10L)).thenReturn(Optional.ofNullable(tolstoyAuthor));
        Mockito.when(authorDao.getByName("Anton Chekhov")).thenReturn(Optional.ofNullable(chekhovAuthor));
        Mockito.when(authorDao.getAll()).thenReturn(Arrays.asList(tolstoyAuthor, chekhovAuthor));
        Mockito.when(bookService.getByAuthor(any())).thenReturn(Collections.singletonList(any()));
    }

    @BeforeEach
    public void init() {
        authorService = new AuthorServiceImpl(authorDao, bookService);
    }

    @DisplayName("Возвращать автора по id")
    @Test
    void shouldGetById() {
        Author author = authorService.getById(10).get();

        assertThat(author.getName()).isEqualTo("Lev Tolstoy");
    }

    @DisplayName("Возвращать автора по имени")
    @Test
    void getByName() {
        Author author = authorService.getByName("Anton Chekhov").get();

        assertThat(author.getName()).isEqualTo("Anton Chekhov");
    }

    @DisplayName("Возвращать всех авторов")
    @Test
    void getAll() {
        List<Author> actualAuthorList = authorService.getAll();
        assertThat(actualAuthorList).hasSize(2).containsExactly(tolstoyAuthor, chekhovAuthor);
    }

    @DisplayName("Возвращать ошибку при добавлении, если нет имени")
    @Test
    void insert() {
        Author newAuthor = new Author(0, "");

        Throwable thrown = assertThrows(AuthorNotValidException.class, () -> {
            authorService.insert(newAuthor);
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