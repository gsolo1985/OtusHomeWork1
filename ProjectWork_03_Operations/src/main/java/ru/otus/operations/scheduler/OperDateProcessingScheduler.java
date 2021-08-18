package ru.otus.operations.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.operations.publish.newoperday.DateMessage;
import ru.otus.operations.publish.newoperday.DateMessageOutPublishGateway;
import ru.otus.operations.repository.BusinessProcessByOperDateRepository;
import ru.otus.operations.repository.BusinessProcessRepository;
import ru.otus.operations.repository.OperDateRepository;
import ru.otus.operations.service.OperDateService;

import static ru.otus.operations.constants.BusinessProcessConstants.*;

@Service
@RequiredArgsConstructor
public class OperDateProcessingScheduler {
    private final BusinessProcessByOperDateRepository businessProcessByOperDateRepository;
    private final BusinessProcessRepository businessProcessRepository;
    private final OperDateService operDateService;
    private final OperDateRepository operDateRepository;
    private final DateMessageOutPublishGateway dateMessageOutPublishGateway;

    @Scheduled(fixedDelayString = "${schedulerTask.operDateProcessing.interval}")
    public void startDayProcessing() {
        System.out.println(BUSINESS_PROCESS_START_INFO);
        var processesList = businessProcessRepository.findAll(Sort.by("OrderType"));

        for (var process: processesList) {
            String sysName = process.getSysName();

            //Проверка корректности данных на начало дня
            if (sysName.equals(CHECK_START_DAY_DATA_SYS_NAME)) {
                operDateService.fillOperDate();                  //Заполняем БД датами

                System.out.println("ALL DATES = " + operDateRepository.findAll());
            }

            //Открытие операционного дня
            if (sysName.equals(OPEN_OPER_DATE_NAME)) {
                var newDate = operDateService.openOperDay();

                System.out.println("NEW OPER DATE = " + newDate);

                DateMessage dateMessage = DateMessage.builder()
                        .date(newDate.getOperDate())
                        .build();

                dateMessageOutPublishGateway.publish(DateMessage.builder() // отправляем в кафку сообщение, что открыт новый опер. день
                        .date(newDate.getOperDate())
                        .build());
            }

            //Закрытие операционного дня
            if (sysName.equals(CLOSE_OPER_DATE_SYS_NAME)) {
                var openedDay = operDateService.getOperDay();
                openedDay.ifPresent(operDateService::closeOperDay);
                System.out.println("ALL DATES END DAY = " + operDateRepository.findAll());
            }

        }
    }
}
