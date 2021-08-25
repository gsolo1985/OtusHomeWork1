package ru.otus.operations.service.processing;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.exception.AppModeNotValidException;
import ru.otus.operations.publish.newoperday.DateMessageOutPublishGateway;
import ru.otus.operations.service.BusinessProcessByOperDateService;
import ru.otus.operations.service.OperDateService;

import static ru.otus.operations.constants.BusinessProcessConstants.BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED;
import static ru.otus.operations.constants.BusinessProcessConstants.OPEN_OPER_DATE_NAME;

@Service
@RequiredArgsConstructor
public class StartOperDayServiceImpl implements StartOperDayService {
    private final OperDateService operDateService;
    private final BusinessProcessByOperDateService businessProcessByOperDateService;
    private final DateMessageOutPublishGateway dateMessageOutPublishGateway;

    @Value(value = "${app_mode}")
    private int appMode;

    /**
     * Запуск бизнес-процесса с системным именем "OPEN_OPER_DATE_NAME"
     * Открытие операционного дня
     *
     * @return - открытый операционный день
     */
    @Override
    @Transactional
    public OperDateEntity exec() {
        System.out.println("Открытие опер дня");
        if (appMode < 0 || appMode > 1) {
            throw new AppModeNotValidException("Incorrect parameter 'app_mode' in application.yml");
        }

        operDateService.fillOperDate(); //Заполняем БД датами (если это необходимо)
        var newDate = operDateService.openOperDay();

        businessProcessByOperDateService.addBusinessProcessesByOperDate(newDate); // Добавляем в обработку все бизнесс-процессы за дату

//        dateMessageOutPublishGateway.publish(DateMessage.builder() // отправляем в кафку сообщение, что открыт новый опер. день
//                .date(newDate.getOperDate())
//                .build());

        businessProcessByOperDateService.setBusinessProcessesByOperDateStatus(newDate, OPEN_OPER_DATE_NAME, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // пометим бизнес-процесс как обработанный

        return newDate;
    }
}
