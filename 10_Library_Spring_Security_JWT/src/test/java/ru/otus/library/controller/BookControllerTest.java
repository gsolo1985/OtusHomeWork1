package ru.otus.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.BookComment;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.dto.BookCommentDto;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.GenreDto;
import ru.otus.library.dto.mapper.DtoMapper;
import ru.otus.library.repositoriy.AuthorRepository;
import ru.otus.library.repositoriy.BookCommentRepository;
import ru.otus.library.repositoriy.BookRepository;
import ru.otus.library.repositoriy.GenreRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Тестирование контроллера для Book")
class BookControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    DtoMapper dtoMapper;
    @Autowired
    private BookRepository repository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookCommentRepository bookCommentRepository;
    private String token;

    private static final String ORWELL_AUTHOR_NAME = "George Orwell";
    private static final String TOLSTOY_AUTHOR_NAME = "Lev Tolstoy";
    private static final String DOSTAEVSKIY_AUTHOR_NAME = "Fedor Dostaevskiy";
    private static final String DYSTOPIA_GENRE_NAME = "Dystopia";
    private static final String DRAMA_GENRE_NAME = "Drama";
    private static final String COMMENT_GOOD = "Good";
    private static final String COMMENT_GOOD2 = "Good2";
    private static final String COMMENT_DISLIKE = "Dislike";
    private static final String COMMENT_BAD = "Bad";
    private static final String UNCLE_VANYA_BOOK_TITLE = "Uncle Vanya";
    private static final String WAR_AND_PEACE_BOOK_TITLE = "War and Peace";
    private static final String CRIME_BOOK_TITLE = "Crime and Punishment";
    private static final String NEW_TITLE_BOOK_TITLE = "New Tittle";

    @BeforeEach
    void setUp() throws Exception {
        var result = mvc.perform(post("/token")
                .header("Authorization", "Basic YWRtaW46cGFzcw=="))
                .andExpect(status().isOk())
                .andReturn();
        token = result.getResponse().getContentAsString();
    }

    @Test
    @DisplayName("получение всех книг")
    @Transactional
    void getAllBooks() throws Exception {
        var booksDto = repository.findAll().stream()
                .map(dtoMapper::bookToDto)
                .collect(Collectors.toList());

        mvc.perform(get("/books")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(booksDto)));
    }


    @Test
    @Transactional(readOnly = true)
    @DisplayName("получение книги по id")
    void getBookById() throws Exception {
        var books = repository.findByTitle(UNCLE_VANYA_BOOK_TITLE);
        assertThat(books).isNotEmpty();

        var expectedBook = dtoMapper.bookToDto(books.get(0));
        assertThat(expectedBook).isNotNull();

        mvc.perform(get("/books/"+expectedBook.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBook)));
    }

    @Test
    @Transactional
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("добавление книги")
    void saveBook() throws Exception {
        AuthorDto authorDto = AuthorDto.builder().name(ORWELL_AUTHOR_NAME).build();
        GenreDto genreDto = GenreDto.builder().name(DYSTOPIA_GENRE_NAME).build();
        BookCommentDto commentDto1 = BookCommentDto.builder().comment(COMMENT_GOOD).build();
        BookCommentDto commentDto2 = BookCommentDto.builder().comment(COMMENT_BAD).build();

        BookDto bookDto = BookDto.builder()
                .author(authorDto)
                .genre(genreDto)
                .title("1984")
                .comments(Arrays.asList(commentDto1, commentDto2))
                .build();

        mvc.perform(post("/books/book")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk());

        var authorAdd = authorRepository.findByName(ORWELL_AUTHOR_NAME).orElse(null);
        assertThat(authorAdd).isNotNull();

        var bookList = repository.findByAuthor(authorAdd);
        assertThat(bookList).isNotEmpty();

        var bookAdd = bookList.get(0);
        assertThat(dtoMapper.bookToDto(bookAdd)).usingRecursiveComparison().ignoringFields("id", "author.id", "genre.id", "comments").isEqualTo(bookDto);

        List<BookComment> comments = bookAdd.getComments();
        BookComment comment1 = BookComment.builder().comment(COMMENT_GOOD).book(bookAdd).build();
        BookComment comment2 = BookComment.builder().comment(COMMENT_BAD).book(bookAdd).build();
        assertThat(comments).usingElementComparatorIgnoringFields("id", "book")
                .contains(comment1)
                .contains(comment2);
    }

    @Test
    @Transactional
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("изменение книги")
    void updateBook() throws Exception {
        AuthorDto authorDto = dtoMapper.authorToDto(authorRepository.findByName(TOLSTOY_AUTHOR_NAME).get());
        GenreDto genreDto = dtoMapper.genreToDto(genreRepository.findByName(DRAMA_GENRE_NAME).get());
        BookCommentDto commentDto1 = BookCommentDto.builder().comment(COMMENT_GOOD).build();
        BookCommentDto commentDto2 = BookCommentDto.builder().comment(COMMENT_BAD).build();

        var author = authorRepository.findByName(TOLSTOY_AUTHOR_NAME).orElse(null);
        assertThat(author).isNotNull();
        var bookForUpdate = repository.findByTitleAndAuthor(WAR_AND_PEACE_BOOK_TITLE, author).orElse(null);
        assertThat(bookForUpdate).isNotNull();

        var bookDto = dtoMapper.bookToDto(bookForUpdate);
        bookDto.setTitle(NEW_TITLE_BOOK_TITLE);

        mvc.perform(put("/books/book")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk());

        var updatedBook = repository.findById(bookDto.getId()).orElse(null);
        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getTitle()).isEqualTo(NEW_TITLE_BOOK_TITLE);

        var updatedBookDto = dtoMapper.bookToDto(updatedBook);
        assertThat(updatedBookDto).isEqualTo(bookDto);
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("удаление книги по id")
    void deleteById() throws Exception {
        var book = repository.findById(1L);
        assertThat(book).isNotEmpty();

        mvc.perform(delete("/books/" + book.get().getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        var delBook = repository.findById(1L);

        assertThat(delBook).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("добавлять комментарии по книге")
    void saveBookComment() throws Exception {
        Author author = authorRepository.findByName(DOSTAEVSKIY_AUTHOR_NAME).get();
        var book = repository.findByTitleAndAuthor(CRIME_BOOK_TITLE, author).orElse(null);
        assertThat(book).isNotNull();

        long id = book.getId();
        BookCommentDto commentDto = BookCommentDto.builder().comment(COMMENT_GOOD2).build();

        mvc.perform(post("/books/book/" + id +"/comments")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk());

        var bookAfterPost = repository.findById(id).orElse(null);
        assertThat(bookAfterPost).isNotNull();

        var comments = bookAfterPost.getComments();
        var commentSave = dtoMapper.bookCommentDtoToBookComment(commentDto, bookAfterPost);

        assertThat(comments).usingElementComparatorIgnoringFields("id")
                .contains(commentSave);
    }

    @Test
    @Transactional
    @DisplayName("удалять комментарии по книге")
    void deleteBookComment() throws Exception {
        Author author = authorRepository.findByName(DOSTAEVSKIY_AUTHOR_NAME).get();
        var book = repository.findByTitleAndAuthor(CRIME_BOOK_TITLE, author).orElse(null);
        assertThat(book).isNotNull();

        var comments = book.getComments();
        var commentForDel = comments.stream().filter(c -> c.getComment().equals(COMMENT_DISLIKE)).findFirst().orElse(null);
        assertThat(commentForDel).isNotNull();

        mvc.perform(delete("/books/comments/" + commentForDel.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        var commentFind = bookCommentRepository.findById(commentForDel.getId());
        assertThat(commentFind).isEmpty();
    }
}