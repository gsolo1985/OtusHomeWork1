package ru.otus.library.controller;

import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.domain.Author;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.dto.mapper.DtoMapper;
import ru.otus.library.exception.NotFoundException;
import ru.otus.library.service.AuthorService;

import javax.validation.Valid;

@RestController
@Generated
@AllArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final DtoMapper dtoMapper;

    @GetMapping("/authors/{id}")
    public AuthorDto getAuthorById(
            @Valid
            @PathVariable(name = "id") Long id) {
        Author author = authorService.getById(id).orElseThrow(NotFoundException::new);
        return dtoMapper.authorToDto(author);
    }

    @DeleteMapping("/authors/{id}")
    @Transactional
    public ResponseEntity<Void> deleteAuthor(
            @Valid
            @PathVariable(name = "id") Long id) {
        Author author = authorService.getById(id).orElseThrow(NotFoundException::new);
        authorService.delete(author);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/authors/author")
    public AuthorDto saveAuthor(
            @Valid
            @RequestBody AuthorDto authorDto) {
        var author = authorService.save(dtoMapper.authorDtoToAuthor(authorDto));
        authorDto.setId(author.getId());

        return authorDto;
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<String> handleNotFoundException() {
        return ResponseEntity.badRequest().body("No found any authors!");
    }
}
