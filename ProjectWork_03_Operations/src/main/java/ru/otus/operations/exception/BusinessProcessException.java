package ru.otus.operations.exception;

public class BusinessProcessException extends RuntimeException{
    public BusinessProcessException(String message) {
        super(message);
    }
}
