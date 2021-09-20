package ru.otus.operations.service.processing;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.operations.constants.Constants;
import ru.otus.operations.exception.BusinessProcessException;
import ru.otus.operations.exception.OpenDateNotValidException;
import ru.otus.operations.service.ProtocolService;
import ru.otus.operations.service.BusinessProcessService;
import ru.otus.operations.service.OperDateService;

import static ru.otus.operations.constants.BusinessProcessConstants.*;

@Service
@RequiredArgsConstructor
public class ProcessingServiceImpl implements ProcessingService {
    private final BusinessProcessService businessProcessService;
    private final StartOperDayService startOperDayService;
    private final GenerateOperationService generateOperationService;
    private final OperDateService operDateService;
    private final CancelOperationService cancelOperationService;
    private final ExecOperationService execOperationService;
    private final RevalOperationService revalOperationService;
    private final ProtocolService protocolService;

    /**
     * Начать обработку операционного дня:
     * Открывается новый операционный день и выполняются настроенные бизнес-процессы.
     */
    @Override
    public void startProcessingOperDay() {
        System.out.println(BUSINESS_PROCESS_START_INFO);
        var processesList = businessProcessService.findAll(Sort.by("OrderType"));

        for (var process : processesList) {
            String sysName = process.getSysName();
            int isOn = process.getIsOn();

            if (isOn == 1) {
                //Открытие операционного дня
                if (sysName.equals(OPEN_OPER_DATE_NAME)) {
                    startOperDayService.exec();
                }

                var od = operDateService.getOperDay();

                if (od.isEmpty()) {
                    throw new OpenDateNotValidException("Operation day isn't defined");
                }

                var openedDay = od.get();

                //Заведение операций
                if (sysName.equals(OPERATIONS_CREATE_SYS_NAME)) {
                    generateOperationService.exec(openedDay);
                }

                //Отмена операций
                if (sysName.equals(OPERATIONS_CANCEL_SYS_NAME)) {
                    cancelOperationService.exec(openedDay);
                }

                //Валютная переоценка
                if (sysName.equals(OPERATIONS_CURRENCY_REVAL_SYS_NAME)) {
                    revalOperationService.exec(openedDay);
                }

                //Исполнение операций
                if (sysName.equals(OPERATIONS_EXECUTION_SYS_NAME)) {
                    execOperationService.exec(openedDay);
                }
            }

        }
    }

    /**
     * Завершение обработки операционного дня. Закрытие операционного дня.
     */
    @Override
    public void stopProcessingOperDay() {
        operDateService.getOperDay().ifPresent(operDay -> {
            operDay.setStatus(Constants.OperDateStatus.CLOSE.ordinal());
            operDateService.closeOperDay(operDay);

            var bpOpt = businessProcessService.findBySysName(CLOSE_OPER_DATE_SYS_NAME);
            if (bpOpt.isEmpty()) {
                throw new BusinessProcessException("No found business process by sys name: CLOSE_OPER_DATE_SYS_NAME");
            }
            protocolService.saveByBusinessProcessesAndOperDate(bpOpt.get(), operDay, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // добавим обработанный протокол
        });

    }
}
