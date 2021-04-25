package ru.otus.library.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.AuthorDeleteException;
import ru.otus.library.exception.AuthorNotValidException;
import ru.otus.library.exception.GenreDeleteException;
import ru.otus.library.exception.GenreNotValidException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование GenreServiceImpl")
class GenreServiceImplTest {
    private GenreService genreService;

    @Mock
    GenreDao genreDao;
    @Mock
    BookService bookService;

    private Genre dramaGenre;
    private Genre novellaGenre;

    @BeforeAll
    public void setUp() {
        dramaGenre = new Genre(10, "Drama");
        novellaGenre = new Genre(11, "Novella");

        Mockito.when(genreDao.getById(10L)).thenReturn(Optional.ofNullable(dramaGenre));
        Mockito.when(genreDao.getByName("Novella")).thenReturn(Optional.ofNullable(novellaGenre));
        Mockito.when(genreDao.getAll()).thenReturn(Arrays.asList(dramaGenre, novellaGenre));
        Mockito.when(bookService.getByGenre(any())).thenReturn(Collections.singletonList(any()));
    }

    @BeforeEach
    public void init() {
        genreService = new GenreServiceImpl(genreDao, bookService);
    }

    @DisplayName("Возвращать жанр по id")
    @Test
    void shouldGetById() {
        Genre genre = genreService.getById(10).get();

        assertThat(genre.getName()).isEqualTo("Drama");
    }

    @DisplayName("Возвращать жанр по имени")
    @Test
    void getByName() {
        Genre genre = genreService.getByName("Novella").get();

        assertThat(genre.getName()).isEqualTo("Novella");
    }

    @DisplayName("Возвращать все жанры")
    @Test
    void getAll() {
        List<Genre> actualGenreList = genreService.getAll();
        assertThat(actualGenreList).hasSize(2).containsExactly(dramaGenre, novellaGenre);
    }

    @DisplayName("Возвращать ошибку при добавлении, если нет имени")
    @Test
    void insert() {
        Genre newGenre = new Genre(0, "");

        Throwable thrown = assertThrows(GenreNotValidException.class, () -> {
            genreService.insert(newGenre);
        });

        assertThat(thrown.getMessage()).isNotNull();
    }

    @DisplayName("Возвращать ошибку при удалении если книги с этим жанром")
    @Test
    void delete() {
        Throwable thrown = assertThrows(GenreDeleteException.class, () -> {
            genreService.delete(dramaGenre);
        });

        assertThat(thrown.getMessage()).isNotNull();
    }

}