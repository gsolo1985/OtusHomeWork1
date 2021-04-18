package ru.otus.studenttest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OutputQuestionsServiceImpl implements OutputQuestionsService {
    private final ReadCsvFileService readCsvFileService;
    private List<String[]> questionList;
    private final MessageSource messageSource;

    @Override
    public void outputAllQuestions() throws IOException {
        for (String[] question : getActualQuestionList()) {
            System.out.println(question[0]);
        }
    }

    @Override
    public void outputQuestion(int questionNumber) throws IOException {
        if (getCountQuestions() > questionNumber) {
            System.out.println(messageSource.getMessage(getActualQuestionList().get(questionNumber)[0], null, Locale.getDefault()));
        }
    }

    @Override
    public String getSolutionAnswer(int questionNumber) throws IOException {
        if (getCountQuestions() > questionNumber) {
            return messageSource.getMessage(getActualQuestionList().get(questionNumber)[1], null, Locale.getDefault());
        } else return "";
    }

    @Override
    public int getCountQuestions() throws IOException {
        return getActualQuestionList().size();
    }

    private List<String[]> getActualQuestionList() throws IOException {
        if (questionList == null) {
            questionList = readCsvFileService.getCSVReaderFromResourceFile().readAll();
        }
        return questionList;
    }
}
