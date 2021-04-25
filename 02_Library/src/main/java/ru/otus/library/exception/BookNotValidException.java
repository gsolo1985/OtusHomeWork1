package ru.otus.library.exception;

public class BookNotValidException extends RuntimeException {

    public BookNotValidException(String message) {
        super(message);
    }
}
