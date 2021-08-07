package ru.otus.spring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Generated
public class BookDto {
    private long id;
    private String title;
    private AuthorDto author;
    private GenreDto genre;
    private List<BookCommentDto> comments;
}
