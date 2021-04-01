package ru.otus.studenttest.service;

import java.io.IOException;

public interface OutputQuestionsService {
    void outputAllQuestions() throws IOException;

    void outputQuestion(int questionNumber);

    String getSolutionAnswer(int questionNumber);

    int getCountQuestions();
}
