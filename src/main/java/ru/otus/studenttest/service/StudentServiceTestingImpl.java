package ru.otus.studenttest.service;

import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.studenttest.config.ApplicationSettings;
import ru.otus.studenttest.domain.Student;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Data
@Service
public class StudentServiceTestingImpl implements StudentServiceTesting {
    private final ReadConsoleService readConsoleService;
    private final OutputQuestionsService outputQuestionsService;
    private final ApplicationSettings applicationSettings;
    private final MessageSource messageSource;

    private int correctAnswerCount;

    public StudentServiceTestingImpl(ReadConsoleService readConsoleService, OutputQuestionsService outputQuestionsService, ApplicationSettings applicationSettings, MessageSource messageSource) {
        this.readConsoleService = readConsoleService;
        this.outputQuestionsService = outputQuestionsService;
        this.applicationSettings = applicationSettings;
        this.messageSource = messageSource;
    }

    @Override
    public void startTesting() throws IOException {
        System.out.println("applicationSettings = " + applicationSettings);

        setLocale();
        Student student = new Student();
        int questionCount = outputQuestionsService.getCountQuestions();
        correctAnswerCount = 0;

        System.out.println(messageSource.getMessage("student.welcome", null, Locale.getDefault()));

        student.setName(readConsoleService.readStringInfo());

        for (int i = 0; i < questionCount; i++) {
            outputQuestionsService.outputQuestion(i);
            System.out.println(messageSource.getMessage("student.answer", null, Locale.getDefault()));

            if (checkCorrectAnswer(readConsoleService.readStringInfo(), outputQuestionsService.getSolutionAnswer(i))) {
                correctAnswerCount++;
            }
        }

        if (correctAnswerCount >= applicationSettings.getTrueAnswers())
            System.out.println(messageSource.getMessage("test.result.completed", new Object[]{student.getName()}, Locale.getDefault()));
        else
            System.out.println(messageSource.getMessage("test.result.failed", new Object[]{student.getName()}, Locale.getDefault()));

        System.out.println(messageSource.getMessage("student.result", new Object[]{correctAnswerCount + "/" + questionCount}, Locale.getDefault()));
    }

    private boolean checkCorrectAnswer(String answer, String solution) {
        return answer.toUpperCase().equals(solution.toUpperCase());
    }

    private void setLocale() {
        List<String> localeList = Arrays.asList(applicationSettings.getLocale().split("_"));
        if(!localeList.isEmpty() && localeList.size() > 1) {
            Locale.setDefault(new Locale(localeList.get(0), localeList.get(1)));
        }
    }
}
