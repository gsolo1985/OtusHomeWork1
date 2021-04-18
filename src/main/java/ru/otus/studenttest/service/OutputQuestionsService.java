package ru.otus.studenttest.service;

import java.io.IOException;

public interface OutputQuestionsService {
    void outputAllQuestions() throws IOException;

    void outputQuestion(int questionNumber) throws IOException;

    String getSolutionAnswer(int questionNumber) throws IOException;

    int getCountQuestions() throws IOException;
}
