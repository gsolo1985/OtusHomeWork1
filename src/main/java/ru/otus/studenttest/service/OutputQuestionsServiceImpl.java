package ru.otus.studenttest.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class OutputQuestionsServiceImpl implements OutputQuestionsService {
    private final ReadCsvFileService readCsvFileService;
    private final List<String[]> questionList;

    public OutputQuestionsServiceImpl(ReadCsvFileService readCsvFileService) throws IOException {
        this.readCsvFileService = readCsvFileService;
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
            System.out.println(questionList.get(questionNumber)[0]);
        }
    }

    @Override
    public String getSolutionAnswer(int questionNumber) {
        if (getCountQuestions() > questionNumber) {
            return questionList.get(questionNumber)[1];
        } else return "";
    }

    @Override
    public int getCountQuestions() {
        return questionList.size();
    }
}
