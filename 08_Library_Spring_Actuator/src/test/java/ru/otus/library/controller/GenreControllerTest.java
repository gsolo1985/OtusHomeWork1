package ru.otus.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.GenreDto;
import ru.otus.library.dto.mapper.DtoMapper;
import ru.otus.library.repositoriy.GenreRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Тестирование контроллера для Genre")
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
class GenreControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    DtoMapper dtoMapper;
    @Autowired
    private GenreRepository repository;

    private static final long FIRST_GENRE_ID = 1L;
    private static final String FIRST_GENRE_NAME = "Drama";
    private static final String NEW_GENRE_NAME = "New";
    private static final String FOR_DELETE_GENRE_NAME = "For delete";

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    @DisplayName("вернуть жанр по id")
    void getGenreById() throws Exception {
        Genre genre = Genre.builder()
                .id(FIRST_GENRE_ID)
                .name(FIRST_GENRE_NAME)
                .build();

        GenreDto expectedGenre = dtoMapper.genreToDto(genre);

        mvc.perform(get("/genres/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedGenre)));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    @DisplayName("удалить жанр по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteGenre() throws Exception {
        var delGenre = repository.findByName(FOR_DELETE_GENRE_NAME);

        mvc.perform(MockMvcRequestBuilders
                .delete("/genres/" + delGenre.get().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    @DisplayName("создать жанр")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void saveGenre() throws Exception {
        GenreDto genreDto = GenreDto.builder()
                .name(NEW_GENRE_NAME)
                .build();

        mvc.perform(post("/genres/genre")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(genreDto)))
                .andExpect(status().isOk());

        var genre = repository.findByName(NEW_GENRE_NAME).orElse(null);
        assertThat(genre).isNotNull();

        var expectedGenre = dtoMapper.genreToDto(genre);

        mvc.perform(get("/genres/" + expectedGenre.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedGenre)));
    }
}