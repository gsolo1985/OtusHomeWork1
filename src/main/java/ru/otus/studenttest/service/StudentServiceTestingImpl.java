package ru.otus.studenttest.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.studenttest.config.ApplicationSettings;
import ru.otus.studenttest.domain.Student;

import java.io.IOException;
import java.util.Locale;

@Data
@Service
@RequiredArgsConstructor
public class StudentServiceTestingImpl implements StudentServiceTesting {
    private final ReadConsoleService readConsoleService;
    private final OutputQuestionsService outputQuestionsService;
    private final ApplicationSettings applicationSettings;
    private final MessageSource messageSource;
    private int correctAnswerCount;

    @Override
    public void startTesting() throws IOException {
        int questionCount = outputQuestionsService.getCountQuestions();
        correctAnswerCount = 0;

        for (int i = 0; i < questionCount; i++) {
            outputQuestionsService.outputQuestion(i);
            System.out.println(messageSource.getMessage("student.answer", null, Locale.getDefault()));

            if (checkCorrectAnswer(readConsoleService.readStringInfo(), outputQuestionsService.getSolutionAnswer(i))) {
                correctAnswerCount++;
            }
        }

        if (correctAnswerCount >= applicationSettings.getTrueAnswers())
            System.out.println(messageSource.getMessage("test.result.completed", null, Locale.getDefault()));
        else
            System.out.println(messageSource.getMessage("test.result.failed", null, Locale.getDefault()));

        System.out.println(messageSource.getMessage("student.result", new Object[]{correctAnswerCount + "/" + questionCount}, Locale.getDefault()));
    }

    @Override
    public Student getNewStudent() throws IOException {
        Student student = new Student();

        System.out.println(messageSource.getMessage("student.welcome", null, Locale.getDefault()));
        student.setName(readConsoleService.readStringInfo());

        return student;
    }

    private boolean checkCorrectAnswer(String answer, String solution) {
        return answer.toUpperCase().equals(solution.toUpperCase());
    }

}
