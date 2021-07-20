package ru.otus.spring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BookComment {
    private long id;
    @ToString.Exclude
    private Book book;
    private String comment;
}
