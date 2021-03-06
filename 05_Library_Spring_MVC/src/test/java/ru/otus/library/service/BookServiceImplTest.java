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
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        warAndPeaceBook = Book.builder().id(WAR_AND_PEACE_BOOK_ID).title(WAR_AND_PEACE_BOOK_TITLE).genre(dramaGenre).author(tolstoyAuthor).build();
        uncleVanyaBook = Book.builder().id(UNCLE_VANYA_BOOK_MOCK_ID).title(UNCLE_VANYA_BOOK_TITLE).genre(novellaGenre).author(chekhovAuthor).build();
    }


    @BeforeEach
    public void init() {
        bookService = new BookServiceImpl(bookRepository, bookCommentService);
    }

    @DisplayName("Возвращать книгу по id")
    @Test
    void shouldGetById() {
        Mockito.when(bookRepository.findById(WAR_AND_PEACE_BOOK_ID)).thenReturn(Optional.ofNullable(warAndPeaceBook));
        Book book = bookService.getById(WAR_AND_PEACE_BOOK_ID).get();

        assertThat(book.getTitle()).isEqualTo(WAR_AND_PEACE_BOOK_TITLE);
    }

    @DisplayName("Возвращать все книги")
    @Test
    void getAll() {
        Mockito.when(bookRepository.findAll()).thenReturn(Arrays.asList(warAndPeaceBook, uncleVanyaBook));
        List<Book> actualBookList = bookService.getAll();
        assertThat(actualBookList).hasSize(EXPECTED_NUMBER_BOOKS_2).containsExactly(warAndPeaceBook, uncleVanyaBook);
    }

    @DisplayName("Возвращать все книги по автору")
    @Test
    void getByAuthor() {
        Mockito.when(bookRepository.findByAuthor(tolstoyAuthor)).thenReturn(Collections.singletonList(warAndPeaceBook));
        List<Book> actualBookList = bookService.getByAuthor(tolstoyAuthor);
        assertThat(actualBookList).hasSize(EXPECTED_NUMBER_BOOKS_1).containsExactly(warAndPeaceBook);
    }

    @DisplayName("Возвращать все книги по жанру")
    @Test
    void getByGenre() {
        Mockito.when(bookRepository.findByGenre(novellaGenre)).thenReturn(Collections.singletonList(uncleVanyaBook));
        List<Book> actualBookList = bookService.getByGenre(novellaGenre);
        assertThat(actualBookList).hasSize(EXPECTED_NUMBER_BOOKS_1).containsExactly(uncleVanyaBook);
    }

    @DisplayName("Возвращать ошибку при добавлении если нет жанра или автора")
    @Test
    void insert() {
        Book newBook = Book.builder().id(0).title("test").build();

        Throwable thrown = assertThrows(BookNotValidException.class, () -> {
            bookService.insert(newBook);
        });

        assertThat(thrown.getMessage()).isNotNull();
    }

}