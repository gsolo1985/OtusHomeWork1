package ru.otus.library.controller;

import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookComment;
import ru.otus.library.dto.BookCommentDto;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.mapper.DtoMapper;
import ru.otus.library.exception.AuthorForBookNotFoundException;
import ru.otus.library.exception.CommentNotFoundException;
import ru.otus.library.exception.GenreForBookNotFoundException;
import ru.otus.library.exception.NotFoundException;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookCommentService;
import ru.otus.library.service.BookService;
import ru.otus.library.service.GenreService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Generated
@AllArgsConstructor
public class BookController {
    private final BookService bookService;
    private final DtoMapper dtoMapper;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookCommentService bookCommentService;

    @GetMapping("/books")
    public List<BookDto> getAllBooks() {
        return bookService.getAll().stream()
                .map(dtoMapper::bookToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/books/{id}")
    public BookDto getBookById(
            @Valid
            @PathVariable(name = "id") Long id) {
        Book book = bookService.getById(id).orElseThrow(NotFoundException::new);
        return dtoMapper.bookToDto(book);
    }

    @PostMapping("/books/book")
    public BookDto saveBook(
            @Valid
            @RequestBody BookDto bookDto) {
        var authorDto = Optional.ofNullable(bookDto.getAuthor()).orElseThrow(AuthorForBookNotFoundException::new);
        var genreDto = Optional.ofNullable(bookDto.getGenre()).orElseThrow(GenreForBookNotFoundException::new);

        var book = bookService.insert(dtoMapper.bookDtoToBook(bookDto));
        bookDto.setId(book.getId());

        return bookDto;
    }

    @PostMapping("/books/book/{id}/comments")
    public BookCommentDto saveBookComment(
            @Valid
            @PathVariable(name = "id") Long id,
            @Valid
            @RequestBody BookCommentDto bookCommentDto) {
        var book = bookService.getById(id).orElseThrow(NotFoundException::new);
        var bookCommentSave = bookCommentService.save(dtoMapper.bookCommentDtoToBookComment(bookCommentDto, book));
        bookCommentDto.setId(bookCommentSave.getId());

        return bookCommentDto;
    }

    @PutMapping("/books/book")
    public BookDto updateBook(
            @Valid
            @RequestBody BookDto bookDto) {
        var authorDto = Optional.ofNullable(bookDto.getAuthor()).orElseThrow(AuthorForBookNotFoundException::new);
        var genreDto = Optional.ofNullable(bookDto.getGenre()).orElseThrow(GenreForBookNotFoundException::new);

        var book = bookService.update(dtoMapper.bookDtoToBook(bookDto));
        return bookDto;
    }

    @DeleteMapping("/books/comments/{id}")
    @Transactional
    public ResponseEntity<Void> deleteBookComments(
            @Valid
            @PathVariable(name = "id") Long id) {
        BookComment bookComment = bookCommentService.getById(id).orElseThrow(CommentNotFoundException::new);
        bookCommentService.delete(bookComment);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/books/{id}")
    @Transactional
    public ResponseEntity<Void> deleteBook(
            @Valid
            @PathVariable(name = "id") Long id) {
        Book book = bookService.getById(id).orElseThrow(NotFoundException::new);
        bookService.delete(book);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<String> handleNotFoundException() {
        return ResponseEntity.badRequest().body("No found any books!");
    }

    @ExceptionHandler(AuthorForBookNotFoundException.class)
    private ResponseEntity<String> handleAuthorForBookNotFoundException() {
        return ResponseEntity.badRequest().body("Author for new book did't find!");
    }

    @ExceptionHandler(GenreForBookNotFoundException.class)
    private ResponseEntity<String> handleGenreForBookNotFoundException() {
        return ResponseEntity.badRequest().body("Genre for new book did't find!");
    }

    @ExceptionHandler(CommentNotFoundException.class)
    private ResponseEntity<String> handleCommentForBookNotFoundException() {
        return ResponseEntity.badRequest().body("No found book comment!");
    }
}
