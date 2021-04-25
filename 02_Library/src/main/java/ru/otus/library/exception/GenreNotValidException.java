package ru.otus.library.exception;

public class GenreNotValidException extends RuntimeException{

    public GenreNotValidException(String message) {
        super(message);
    }
}
