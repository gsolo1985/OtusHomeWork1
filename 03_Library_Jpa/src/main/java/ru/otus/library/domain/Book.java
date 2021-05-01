package ru.otus.library.domain;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

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

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(targetEntity = BookComment.class, mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<BookComment> comments;
}
