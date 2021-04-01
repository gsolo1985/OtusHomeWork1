package ru.otus.studenttest.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.studenttest.domain.Student;

import java.io.IOException;

@Data
@Service
public class StudentServiceTestingImpl implements StudentServiceTesting {
    private final ReadConsoleService readConsoleService;
    private final OutputQuestionsService outputQuestionsService;
    @Value("${count.trueAnswers}")
    private int completeTestCount;
    private int correctAnswerCount;

    public StudentServiceTestingImpl(ReadConsoleService readConsoleService, OutputQuestionsService outputQuestionsService) {
        this.readConsoleService = readConsoleService;
        this.outputQuestionsService = outputQuestionsService;
    }

    @Override
    public void startTesting() throws IOException {
        Student student = new Student(); //или лучше через dao?
        int questionCount = outputQuestionsService.getCountQuestions();
        correctAnswerCount = 0;

        System.out.println("Hello, enter your name: ");
        student.setName(readConsoleService.readStringInfo());

        for (int i = 0; i < questionCount; i++) {
            outputQuestionsService.outputQuestion(i);
            System.out.println("Please, enter your answer: ");

            if (checkCorrectAnswer(readConsoleService.readStringInfo(), outputQuestionsService.getSolutionAnswer(i))) {
                correctAnswerCount++;
            }
        }

        System.out.printf(correctAnswerCount >= completeTestCount ? "Well done, %s! Test is completed! \n" : "Sorry, %s. Test is failed.\n", student.getName());
        System.out.println("Your result - " + correctAnswerCount + "/" + questionCount);
    }

    private boolean checkCorrectAnswer(String answer, String solution) {
        return answer.toUpperCase().equals(solution.toUpperCase());
    }
}
