package ru.otus.library.exception;

public class AuthorNotValidException extends RuntimeException{

    public AuthorNotValidException(String message) {
        super(message);
    }
}
