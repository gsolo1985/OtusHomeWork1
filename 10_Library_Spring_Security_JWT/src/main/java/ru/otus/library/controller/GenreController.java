package ru.otus.library.controller;

import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.GenreDto;
import ru.otus.library.dto.mapper.DtoMapper;
import ru.otus.library.exception.NotFoundException;
import ru.otus.library.service.GenreService;

import javax.validation.Valid;

@RestController
@Generated
@AllArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private final DtoMapper dtoMapper;

    @GetMapping("/genres/{id}")
    public GenreDto getGenreById(
            @Valid
            @PathVariable(name = "id") Long id) {
        Genre genre = genreService.getById(id).orElseThrow(NotFoundException::new);
        return dtoMapper.genreToDto(genre);
    }

    @DeleteMapping("/genres/{id}")
    @Transactional
    public ResponseEntity<Void> deleteGenre(
            @Valid
            @PathVariable(name = "id") Long id) {
        Genre genre = genreService.getById(id).orElseThrow(NotFoundException::new);
        genreService.delete(genre);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/genres/genre")
    public GenreDto saveGenre(
            @Valid
            @RequestBody GenreDto genreDto) {
        var genre = genreService.save(dtoMapper.genreDtoToGenre(genreDto));
        genreDto.setId(genre.getId());
        return genreDto;
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<String> handleNotFoundException() {
        return ResponseEntity.badRequest().body("No found any genres!");
    }
}
