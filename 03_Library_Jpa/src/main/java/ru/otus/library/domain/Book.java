package ru.otus.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "book")
@NamedEntityGraph(name = "authors-entity-graph", attributeNodes = {@NamedAttributeNode("author")})
@NamedEntityGraph(name = "genres-entity-graph", attributeNodes = {@NamedAttributeNode("genre")})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", unique = true)
    private String title;

    @ManyToOne(targetEntity = Author.class, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "authorid", nullable = false, foreignKey = @ForeignKey(name = "k02_book"))
    private Author author;

    @ManyToOne(targetEntity = Genre.class, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "genreid", nullable = false, foreignKey = @ForeignKey(name = "k03_book"))
    private Genre genre;
}
