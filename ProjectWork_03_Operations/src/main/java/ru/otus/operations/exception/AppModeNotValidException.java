package ru.otus.operations.exception;

public class AppModeNotValidException extends RuntimeException{
    public AppModeNotValidException(String message) {
        super(message);
    }
}
