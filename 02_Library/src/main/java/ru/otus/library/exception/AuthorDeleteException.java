package ru.otus.library.exception;

public class AuthorDeleteException extends RuntimeException {

    public AuthorDeleteException(String message) {
        super(message);
    }
}