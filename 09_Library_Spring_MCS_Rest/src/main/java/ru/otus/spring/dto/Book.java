package ru.otus.spring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Book {
    private long id;
    private String title;
    private Genre genre;
    private Author author;
    private List<BookComment> comments;
}
