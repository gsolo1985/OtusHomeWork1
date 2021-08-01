package ru.otus.library.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.GenreDeleteException;
import ru.otus.library.exception.GenreNotValidException;
import ru.otus.library.repositoriy.GenreRepository;

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
@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {
    private static final String DRAMA_GENRE_NAME = "Drama";
    private static final String NOVELLA_GENRE_NAME = "Novella";
    private static final long DRAMA_GENRE_MOCK_ID = 10L;
    private static final long NOVELLA_GENRE_MOCK_ID = 11L;
    private static final int EXPECTED_NUMBER_OF_GENRES = 2;

    private GenreService genreService;

    @Mock
    private GenreRepository genreRepository;
    @Mock
    private BookService bookService;

    private Genre dramaGenre;
    private Genre novellaGenre;

    @BeforeAll
    public void setUp() {
        dramaGenre = new Genre(DRAMA_GENRE_MOCK_ID, DRAMA_GENRE_NAME);
        novellaGenre = new Genre(NOVELLA_GENRE_MOCK_ID, NOVELLA_GENRE_NAME);
    }

    @BeforeEach
    public void init() {
        genreService = new GenreServiceImpl(genreRepository, bookService);
    }

    @DisplayName("Возвращать жанр по id")
    @Test
    void shouldGetById() {
        Mockito.when(genreRepository.findById(DRAMA_GENRE_MOCK_ID)).thenReturn(Optional.ofNullable(dramaGenre));
        Genre genre = genreService.getById(DRAMA_GENRE_MOCK_ID).orElse(null);
        assertThat(genre).isNotNull();

        assertThat(genre.getName()).isEqualTo(DRAMA_GENRE_NAME);
    }

    @DisplayName("Возвращать жанр по имени")
    @Test
    void getByName() {
        Mockito.when(genreRepository.findByName(NOVELLA_GENRE_NAME)).thenReturn(Optional.ofNullable(novellaGenre));
        Genre genre = genreService.getByName(NOVELLA_GENRE_NAME).orElse(null);
        assertThat(genre).isNotNull();

        assertThat(genre.getName()).isEqualTo(NOVELLA_GENRE_NAME);
    }

    @DisplayName("Возвращать все жанры")
    @Test
    void getAll() {
        Mockito.when(genreRepository.findAll()).thenReturn(Arrays.asList(dramaGenre, novellaGenre));
        List<Genre> actualGenreList = genreService.getAll();
        assertThat(actualGenreList).hasSize(EXPECTED_NUMBER_OF_GENRES).containsExactly(dramaGenre, novellaGenre);
    }

    @DisplayName("Возвращать ошибку при добавлении, если нет имени")
    @Test
    void insert() {
        Genre newGenre = new Genre(0, "");

        Throwable thrown = assertThrows(GenreNotValidException.class, () -> {
            genreService.save(newGenre);
        });

        assertThat(thrown.getMessage()).isNotNull();
    }

    @DisplayName("Возвращать ошибку при удалении если книги с этим жанром")
    @Test
    void delete() {
        Mockito.when(bookService.getByGenre(any())).thenReturn(Collections.singletonList(any()));
        Throwable thrown = assertThrows(GenreDeleteException.class, () -> {
            genreService.delete(dramaGenre);
        });

        assertThat(thrown.getMessage()).isNotNull();
    }
}