package ru.otus.studenttest.service;

import ru.otus.studenttest.domain.Student;

import java.io.IOException;

public interface StudentServiceTesting {
    void startTesting() throws IOException;

    Student loginStudent() throws IOException;
}
