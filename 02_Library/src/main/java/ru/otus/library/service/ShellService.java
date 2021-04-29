package ru.otus.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class ShellService {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @ShellMethod(value = "Author create. Parameters = {authorName}", key = {"ac"})
    public String authorCreate(@ShellOption(help = "authorName") String name) {
        Author author = authorService.insert(new Author(0, name));
        return "Author has created: " + author.getName() + ".";
    }

    @ShellMethod(value = "Author delete. Parameters = {authorName}", key = {"ad"})
    public String authorDelete(@ShellOption(help = "authorName") String name) {
        Optional<Author> author = authorService.getByName(name);
        if (author.isPresent()) {
            authorService.delete(author.get());
            return "Author has deleted.";
        } else
            return "There is no author with this name.";
    }

    @ShellMethod(value = "Author show all", key ={"as"})
    public void authorShowAll() {
        authorService.getAll().forEach(author -> System.out.println(author.getName()));
    }

    @ShellMethod(value = "Genre create, Parameters = {genreName}", key = {"gc"})
    public String genreCreate(@ShellOption(help = "genreName") String name) {
        Genre genre = genreService.insert(new Genre(0, name));
        return "Genre has created: " + genre.getName() + ".";
    }

    @ShellMethod(value = "Genre delete. Parameters = {genreName}", key = {"gd"})
    public String genreDelete(@ShellOption(help = "genreName") String name) {
        Optional<Genre> genre = genreService.getByName(name);

        if (genre.isPresent()) {
            genreService.delete(genre.get());
            return "Genre has deleted.";
        } else
            return "There is no genre with this name";
    }

    @ShellMethod(value = "Genre show all", key ={"gs"})
    public void genreShowAll() {
        genreService.getAll().forEach(genre -> System.out.println(genre.getName()));
    }

    @ShellMethod(value = "Book create. Parameters = {title, authorName, genreName}", key = {"bc"})
    public String bookCreate(@ShellOption(help = "bookTitle") String title,
                             @ShellOption(help = "authorName") String authorName,
                             @ShellOption(help = "genreName") String genreName) {
        Optional<Author> author = authorService.getByName(authorName);
        Optional<Genre> genre = genreService.getByName(genreName);

        if (author.isEmpty())
            return "You must enter correct author, to see all authors in the system please use command - as";

        if (genre.isEmpty())
            return "You must enter correct genre, to see all genres in the system please use command - gs";

        Book book = bookService.insert(new Book(0, title, author.get(), genre.get()));

        return "Book has created: " + book.getTitle() + ". Author = " + author.get().getName() + ". Genre = " + genre.get().getName();
    }

    @ShellMethod(value = "Book show all", key ={"bs"})
    public void bookShowAll() {
        bookService.getAll().forEach(System.out::println);
    }

    @ShellMethod(value = "Book show by title and author. Parameters = {bookTitle, authorName}", key ={"bf"})
    public String bookShowBytitleAndAuthor(@ShellOption(help = "bookTitle") String title,
                                          @ShellOption(help = "authorName") String authorName) {
        Optional<Book> book = bookService.getByTitleAndAuthor(title, authorService.getByName(authorName).orElse(null));
        return book.map(value -> value.getTitle() + ". Author " + value.getAuthor().getName() + ". Genre = " + value.getGenre().getName()).orElse("No book find by your parameters");
    }

    @ShellMethod(value = "Book update. Parameters = {BookId, bookTitle, authorName, genreName}", key = {"bu"})
    public String bookUpdate(@ShellOption(help = "id") Long id,
                             @ShellOption(help = "bookTitle") String title,
                             @ShellOption(help = "authorName") String authorName,
                             @ShellOption(help = "genreName") String genreName) {
        Optional<Author> author = authorService.getByName(authorName);
        Optional<Genre> genre = genreService.getByName(genreName);

        if (author.isEmpty())
            return "You must enter correct author, to see all authors in the system please use command - as";

        if (genre.isEmpty())
            return "You must enter correct genre, to see all genres in the system please use command - gs";

        Optional<Book> book = bookService.getById(id);

        if (book.isPresent()) {
            bookService.update(new Book(id, title, author.get(), genre.get()));
            return "The book has modified.";
        }
        else
            return "The book hasn't find by this id = " + id;
    }

    @ShellMethod(value = "Book show by author. Parameters = {authorName}", key = {"bsa"})
    public void bookShowByAuthor(@ShellOption(help = "authorName") String authorName) {
        Optional<Author> author = authorService.getByName(authorName);

        if (author.isEmpty()) {
            System.out.println("You must enter correct author, to see all authors in the system please use command - as");
        }
        else {
            List<Book> bookList = bookService.getByAuthor(author.get());
            if (bookList.size() > 0) {
                bookList.forEach(System.out::println);
            }
            else
                System.out.println("No books has found by author = " + authorName);
        }
    }
}
