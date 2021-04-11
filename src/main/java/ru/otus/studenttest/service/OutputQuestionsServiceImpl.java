package ru.otus.studenttest.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Service
public class OutputQuestionsServiceImpl implements OutputQuestionsService {
    private final ReadCsvFileService readCsvFileService;
    private final List<String[]> questionList;
    private final MessageSource messageSource;

    public OutputQuestionsServiceImpl(ReadCsvFileService readCsvFileService, MessageSource messageSource) throws IOException {
        this.readCsvFileService = readCsvFileService;
        this.messageSource = messageSource;
        this.questionList = this.readCsvFileService.getCSVReaderFromResourceFile().readAll();
    }

    @Override
    public void outputAllQuestions() throws IOException {
        for (String[] question : questionList) {
            System.out.println(question[0]);
        }
    }

    @Override
    public void outputQuestion(int questionNumber) {
        if (getCountQuestions() > questionNumber) {
            System.out.println(messageSource.getMessage(questionList.get(questionNumber)[0], null, Locale.getDefault()));
        }
    }

    @Override
    public String getSolutionAnswer(int questionNumber) {
        if (getCountQuestions() > questionNumber) {
            return messageSource.getMessage(questionList.get(questionNumber)[1], null, Locale.getDefault());
        } else return "";
    }

    @Override
    public int getCountQuestions() {
        return questionList.size();
    }
}
