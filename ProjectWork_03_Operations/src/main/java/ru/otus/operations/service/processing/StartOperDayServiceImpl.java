package ru.otus.operations.service.processing;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.exception.AppModeNotValidException;
import ru.otus.operations.exception.BusinessProcessException;
import ru.otus.operations.publish.newoperday.DateMessage;
import ru.otus.operations.publish.newoperday.DateMessageOutPublishGateway;
import ru.otus.operations.service.BusinessProcessService;
import ru.otus.operations.service.ProtocolService;
import ru.otus.operations.service.OperDateService;

import static ru.otus.operations.constants.BusinessProcessConstants.BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED;
import static ru.otus.operations.constants.BusinessProcessConstants.OPEN_OPER_DATE_NAME;

@Service
@RequiredArgsConstructor
public class StartOperDayServiceImpl implements StartOperDayService {
    private final OperDateService operDateService;
    private final ProtocolService protocolService;
    private final DateMessageOutPublishGateway dateMessageOutPublishGateway;
    private final BusinessProcessService businessProcessService;

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
        System.out.println("Открытие операционного дня");
        if (appMode < 0 || appMode > 1) {
            throw new AppModeNotValidException("Incorrect parameter 'app_mode' in application.yml");
        }

        operDateService.fillOperDate(); //Заполняем БД датами (если это необходимо)
        var newDate = operDateService.openOperDay();

        dateMessageOutPublishGateway.publish(DateMessage.builder() // отправляем в кафку сообщение, что открыт новый опер. день
                .date(newDate.getOperDate())
                .build());

        var bpOpt = businessProcessService.findBySysName(OPEN_OPER_DATE_NAME);
        if (bpOpt.isEmpty()) {
            throw new BusinessProcessException("No found business process by sys name: OPEN_OPER_DATE_NAME");
        }
        protocolService.saveByBusinessProcessesAndOperDate(bpOpt.get(), newDate, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED);

        return newDate;
    }
}
