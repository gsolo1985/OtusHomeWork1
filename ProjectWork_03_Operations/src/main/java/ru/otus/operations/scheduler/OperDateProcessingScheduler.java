package ru.otus.operations.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.operations.publish.newoperday.DateMessage;
import ru.otus.operations.publish.newoperday.DateMessageOutPublishGateway;
import ru.otus.operations.publish.operationreval.OperationRevalDtoList;
import ru.otus.operations.publish.operationreval.OperationRevalOutPublishGateway;
import ru.otus.operations.repository.BusinessProcessByOperDateRepository;
import ru.otus.operations.repository.BusinessProcessRepository;
import ru.otus.operations.repository.OperDateRepository;
import ru.otus.operations.repository.OperationRepository;
import ru.otus.operations.service.BusinessProcessByOperDateService;
import ru.otus.operations.service.OperDateService;
import ru.otus.operations.service.OperationService;

import java.util.concurrent.TimeUnit;

import static ru.otus.operations.constants.BusinessProcessConstants.*;

@Service
@RequiredArgsConstructor
public class OperDateProcessingScheduler {
    private final BusinessProcessByOperDateRepository businessProcessByOperDateRepository;
    private final BusinessProcessRepository businessProcessRepository;
    private final OperDateService operDateService;
    private final OperDateRepository operDateRepository;
    private final DateMessageOutPublishGateway dateMessageOutPublishGateway;
    private final BusinessProcessByOperDateService businessProcessByOperDateService;
    private final OperationService operationService;
    private final OperationRevalOutPublishGateway operationRevalOutPublishGateway;
    private final OperationRepository operationRepository;

    @Scheduled(fixedDelayString = "${schedulerTask.operDateProcessing.interval}")
    public void startDayProcessing() throws InterruptedException {
        System.out.println(BUSINESS_PROCESS_START_INFO);
        var processesList = businessProcessRepository.findAll(Sort.by("OrderType"));

        for (var process: processesList) {
            String sysName = process.getSysName();

            //Подготовка к открытию операционного дня
            if (sysName.equals(CHECK_START_DAY_DATA_SYS_NAME)) {
                operDateService.fillOperDate();                  //Заполняем БД датами
            }

            //Открытие операционного дня
            if (sysName.equals(OPEN_OPER_DATE_NAME)) {
                var newDate = operDateService.openOperDay();

                businessProcessByOperDateService.addBusinessProcessesByOperDate(newDate); // Добавляем в обработку все бизнесс-процессы за дату

                dateMessageOutPublishGateway.publish(DateMessage.builder() // отправляем в кафку сообщение, что открыт новый опер. день
                        .date(newDate.getOperDate())
                        .build());

                TimeUnit.SECONDS.sleep(3); // ждем 3 секунды, чтобы адаптер по валютам успел отработать

                businessProcessByOperDateService.setBusinessProcessesByOperDateStatus(newDate, OPEN_OPER_DATE_NAME, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // пометим бизнес-процесс как обработанный
            }

            var openedDay = operDateService.getOperDay();

            //Заведение операций
            if (sysName.equals(OPERATIONS_CREATE_SYS_NAME)) {
                openedDay.ifPresent(od -> {
                    operationService.generateByDate(od); // генерим операции

//                    var opers = operationRepository.findAll();
//                    System.out.println("ЗАВЕДЕНИЕ ОПЕРАЦИЙ: ");
//                    opers.forEach(o -> {
//                        System.out.println("id = " + o.getOperationId() + "; operDate = " + o.getOperationDate() + "; planDAte = " + o.getPlanDate() + "; state = " + o.getState() + "; currency = " + o.getCurrencyCashEntity().getName());
//                    });
//                    System.out.println("________________________________");

                    businessProcessByOperDateService.setBusinessProcessesByOperDateStatus(od, OPERATIONS_CREATE_SYS_NAME, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // пометим бизнес-процесс как обработанный
                });
            }

            //Отмена операций
            if (sysName.equals(OPERATIONS_CANCEL_SYS_NAME)) {
                openedDay.ifPresent(od -> {
                    operationService.cancelByPlanDate(od); // отмена плановых операции

//                    var opers = operationRepository.findAll();
//                    System.out.println("ОТМЕНА ОПЕРАЦИЙ: ");
//                    opers.forEach(o -> {
//                        System.out.println("id = " + o.getOperationId() + "; operDate = " + o.getOperationDate() + "; planDAte = " + o.getPlanDate() + "; state = " + o.getState()+ "; currency = " + o.getCurrencyCashEntity().getName());
//                    });
//                    System.out.println("________________________________");

                    businessProcessByOperDateService.setBusinessProcessesByOperDateStatus(od, OPERATIONS_CANCEL_SYS_NAME, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // пометим бизнес-процесс как обработанный
                });
            }

            //Исполнение операций
            if (sysName.equals(OPERATIONS_EXECUTION_SYS_NAME)) {
                openedDay.ifPresent(od -> {
                    operationService.execByPlanDate(od); // исполнение плановых операции

//                    var opers = operationRepository.findAll();
//                    System.out.println("ИСПОЛНЕНИЕ ОПЕРАЦИЙ: ");
//                    opers.forEach(o -> {
//                        System.out.println("id = " + o.getOperationId() + "; operDate = " + o.getOperationDate() + "; planDAte = " + o.getPlanDate() + "; state = " + o.getState()+ "; currency = " + o.getCurrencyCashEntity().getName());
//                    });
//                    System.out.println("________________________________");

                    businessProcessByOperDateService.setBusinessProcessesByOperDateStatus(od, OPERATIONS_EXECUTION_SYS_NAME, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // пометим бизнес-процесс как обработанный
                });
            }

            //Валютная переоценка
            if (sysName.equals(OPERATIONS_CURRENCY_REVAL_SYS_NAME)) {
                openedDay.ifPresent(od -> {
                    var opers = operationService.getOperationForRevalByDate(od);

//                    System.out.println("ОПЕРАЦИИ ДЛЯ ПЕРЕОЦЕНКИ: ");
//                    opers.forEach(o -> {
//                        System.out.println("id = " + o.getOperationId() + "; revalDate = " + o.getRevalDate() + "; currency = " + o.getCurrencyName() + "; amount = " + o.getAmount());
//                    });
//                    System.out.println("________________________________");

                    if (opers != null && opers.size() > 0) {
                        operationRevalOutPublishGateway.publish(OperationRevalDtoList.builder()
                                .operationList(opers).build());
                    }

                    businessProcessByOperDateService.setBusinessProcessesByOperDateStatus(od, OPERATIONS_CURRENCY_REVAL_SYS_NAME, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // пометим бизнес-процесс как обработанный
                });
            }

            //Закрытие операционного дня // todo вынести закрытие дня в отдельный сервис
            if (sysName.equals(CLOSE_OPER_DATE_SYS_NAME)) {
                openedDay.ifPresent(od -> {
                    operDateService.closeOperDay(od);
                    businessProcessByOperDateService.setBusinessProcessesByOperDateStatus(od, CLOSE_OPER_DATE_SYS_NAME, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // пометим бизнес-процесс как обработанный
                });
            }

        }
    }
}
