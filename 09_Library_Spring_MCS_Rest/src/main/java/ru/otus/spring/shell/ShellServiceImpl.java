package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.dto.Book;
import ru.otus.spring.service.BookService;

@ShellComponent
@RequiredArgsConstructor
public class ShellServiceImpl {
    private final BookService bookService;

    @ShellMethod(value = "Book show by Id. Parameters = {id}", key ={"book"})
    public void findBookById(@ShellOption(help = "id") long id) {
        Book book = bookService.getBook(id);
        if (book != null) {
            System.out.println("Book = " + book.getTitle());
            System.out.println("with genre = " + book.getGenre());
            System.out.println("by Author = " + book.getAuthor().getName());
            System.out.println("Comments by this books:");
            book.getComments().forEach(bc -> System.out.println(bc.getComment()));
        }
    }
}
