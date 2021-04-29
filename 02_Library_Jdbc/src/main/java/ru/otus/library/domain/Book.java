package ru.otus.library.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Book {
    private final long id;
    private final String title;
    private final Author author;
    private final Genre genre;
}
