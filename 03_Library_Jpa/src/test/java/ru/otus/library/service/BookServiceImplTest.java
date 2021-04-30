package ru.otus.library.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.BookNotValidException;
import ru.otus.library.repositoriy.BookRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование BookServiceImpl")
class BookServiceImplTest {
    private static final String TOLSTOY_AUTHOR_NAME = "Lev Tolstoy";
    private static final String CHEKHOV_AUTHOR_NAME = "Anton Chekhov";
    private static final long TOLSTOY_AUTHOR_MOCK_ID = 10L;
    private static final long CHEKHOV_AUTHOR_MOCK_ID = 11L;

    private static final String DRAMA_GENRE_NAME = "Drama";
    private static final String NOVELLA_GENRE_NAME = "Novella";
    private static final long DRAMA_GENRE_MOCK_ID = 10L;
    private static final long NOVELLA_GENRE_MOCK_ID = 11L;

    private static final String WAR_AND_PEACE_BOOK_TITLE = "War and Peace";
    private static final String UNCLE_VANYA_BOOK_TITLE = "Uncle Vanya";
    private static final long WAR_AND_PEACE_BOOK_ID = 10L;
    private static final long UNCLE_VANYA_BOOK_MOCK_ID = 11L;

    private static final int EXPECTED_NUMBER_BOOKS_1 = 1;
    private static final int EXPECTED_NUMBER_BOOKS_2 = 2;

    private BookService bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookCommentService bookCommentService;

    private Book warAndPeaceBook;
    private Book uncleVanyaBook;
    private Author tolstoyAuthor;
    private Author chekhovAuthor;
    private Genre dramaGenre;
    private Genre novellaGenre;

    @BeforeAll
    public void setUp() {
        tolstoyAuthor = new Author(TOLSTOY_AUTHOR_MOCK_ID, TOLSTOY_AUTHOR_NAME);
        chekhovAuthor = new Author(CHEKHOV_AUTHOR_MOCK_ID, CHEKHOV_AUTHOR_NAME);
        dramaGenre = new Genre(DRAMA_GENRE_MOCK_ID, DRAMA_GENRE_NAME);
        novellaGenre = new Genre(NOVELLA_GENRE_MOCK_ID, NOVELLA_GENRE_NAME);

        warAndPeaceBook = new Book(WAR_AND_PEACE_BOOK_ID, WAR_AND_PEACE_BOOK_TITLE, tolstoyAuthor, dramaGenre);
        uncleVanyaBook = new Book(UNCLE_VANYA_BOOK_MOCK_ID, UNCLE_VANYA_BOOK_TITLE, chekhovAuthor, novellaGenre);

        Mockito.when(bookRepository.getById(WAR_AND_PEACE_BOOK_ID)).thenReturn(Optional.ofNullable(warAndPeaceBook));
        Mockito.when(bookRepository.getByAuthor(tolstoyAuthor)).thenReturn(Collections.singletonList(warAndPeaceBook));
        Mockito.when(bookRepository.getAll()).thenReturn(Arrays.asList(warAndPeaceBook, uncleVanyaBook));
        Mockito.when(bookRepository.getByGenre(novellaGenre)).thenReturn(Collections.singletonList(uncleVanyaBook));
    }


    @BeforeEach
    public void init() {
        bookService = new BookServiceImpl(bookRepository, bookCommentService);
    }

    @DisplayName("Возвращать книгу по id")
    @Test
    void shouldGetById() {
        Book book = bookService.getById(WAR_AND_PEACE_BOOK_ID).get();

        assertThat(book.getTitle()).isEqualTo(WAR_AND_PEACE_BOOK_TITLE);
    }

    @DisplayName("Возвращать все книги")
    @Test
    void getAll() {
        List<Book> actualBookList = bookService.getAll();
        assertThat(actualBookList).hasSize(EXPECTED_NUMBER_BOOKS_2).containsExactly(warAndPeaceBook, uncleVanyaBook);
    }

    @DisplayName("Возвращать все книги по автору")
    @Test
    void getByAuthor() {
        List<Book> actualBookList = bookService.getByAuthor(tolstoyAuthor);
        assertThat(actualBookList).hasSize(EXPECTED_NUMBER_BOOKS_1).containsExactly(warAndPeaceBook);
    }

    @DisplayName("Возвращать все книги по жанру")
    @Test
    void getByGenre() {
        List<Book> actualBookList = bookService.getByGenre(novellaGenre);
        assertThat(actualBookList).hasSize(EXPECTED_NUMBER_BOOKS_1).containsExactly(uncleVanyaBook);
    }

    @DisplayName("Возвращать ошибку при добавлении если нет жанра или автора")
    @Test
    void insert() {
        Book newBook = new Book(0, "test", null, null);

        Throwable thrown = assertThrows(BookNotValidException.class, () -> {
            bookService.insert(newBook);
        });

        assertThat(thrown.getMessage()).isNotNull();
    }

}