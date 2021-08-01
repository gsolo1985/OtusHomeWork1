package ru.otus.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookDto {
    private long id;
    private String title;
    private AuthorDto author;
    private GenreDto genre;
    private List<BookCommentDto> comments;
}
