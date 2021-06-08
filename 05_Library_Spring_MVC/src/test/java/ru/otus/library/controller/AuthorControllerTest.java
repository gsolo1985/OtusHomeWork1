package ru.otus.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.library.domain.Author;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.dto.mapper.DtoMapper;
import ru.otus.library.repositoriy.AuthorRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Тестирование контроллера для Author")
class AuthorControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    DtoMapper dtoMapper;
    @Autowired
    private AuthorRepository repository;

    private static final long FIRST_AUTHOR_ID = 1L;
    private static final String FIRST_AUTHOR_NAME = "Fedor Dostaevskiy";
    private static final String AHMATOVA_AUTHOR_NAME = "Anna Ahmatova";
    private static final String FOR_DELETE_AUTHOR_NAME = "For delete";

    @Test
    @DisplayName("вернуть автора по id")
    void getAuthorById() throws Exception {
        Author author = Author.builder()
                .id(FIRST_AUTHOR_ID)
                .name(FIRST_AUTHOR_NAME)
                .build();

        AuthorDto expectedAuthor = dtoMapper.authorToDto(author);

        mvc.perform(get("/authors/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedAuthor)));
    }

    @Test
    @DisplayName("удаление автора по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteAuthor() throws Exception {
        var delAuthor = repository.findByName(FOR_DELETE_AUTHOR_NAME);

        mvc.perform(MockMvcRequestBuilders
                .delete("/authors/" + delAuthor.get().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("сохранение нового автора")
    void saveAuthor() throws Exception {
        AuthorDto authorDto = AuthorDto.builder()
                .name(AHMATOVA_AUTHOR_NAME)
                .build();

        mvc.perform(post("/authors/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authorDto)))
                .andExpect(status().isOk());

        var author = repository.findByName(AHMATOVA_AUTHOR_NAME).orElse(null);
        assertThat(author).isNotNull();

        var expectedAuthor = dtoMapper.authorToDto(author);

        mvc.perform(get("/authors/" + expectedAuthor.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedAuthor)));
    }
}