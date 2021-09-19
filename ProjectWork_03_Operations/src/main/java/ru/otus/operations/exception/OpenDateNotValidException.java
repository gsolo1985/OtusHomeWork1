package ru.otus.operations.exception;

public class OpenDateNotValidException extends RuntimeException{
    public OpenDateNotValidException(String message) {
        super(message);
    }
}
