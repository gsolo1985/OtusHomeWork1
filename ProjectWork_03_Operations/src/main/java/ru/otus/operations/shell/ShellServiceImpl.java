package ru.otus.operations.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.operations.model.OperationDto;
import ru.otus.operations.service.OperDateService;
import ru.otus.operations.service.OperationService;
import ru.otus.operations.service.ProtocolInfoService;
import ru.otus.operations.service.ProtocolService;
import ru.otus.operations.service.processing.ProcessingService;
import ru.otus.operations.statemachine.OperationState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.operations.constants.BusinessProcessConstants.*;

@ShellComponent
@RequiredArgsConstructor
public class ShellServiceImpl {
    private final ProcessingService processingService;
    private final OperDateService operDateService;
    private final OperationService operationService;
    private final ProtocolService protocolService;
    private final ProtocolInfoService protocolInfoService;

    @Value(value = "${app_mode}")
    private int appMode;

    @ShellMethod(value = "exec oper day", key = {"start"})
    public void start() throws IOException {
        if (appMode == 0) {
            System.out.println("Приложение запущено в режиме шедуллера");
        } else {
            processingService.startProcessingOperDay();
        }
    }

    @ShellMethod(value = "close oper day", key = {"end"})
    public void end() throws IOException {
        if (appMode == 0) {
            System.out.println("Приложение запущено в режиме шедуллера");
        } else {
            processingService.stopProcessingOperDay();
        }
    }


    @ShellMethod(value = "check data before close day.", key = {"check"})
    public void check(@ShellOption(help = "checkiId") Long checkId) {
        if (appMode == 0) {
            System.out.println("Приложение запущено в режиме шедуллера");
        } else {
            operDateService.getOperDay().ifPresent(operDay -> {
                var operations = operationService.findByOperationDate(operDay.getOperDate());
                var protocols = protocolService.findByOperDate(operDay.getOperDate());

                //1. вывод загруженных операций
                if (checkId == 1) {
                    List<OperationDto> operDto = new ArrayList<>();

                    operations.forEach(o -> {
                        operDto.add(operationService.entityToDto(o));
                    });

                    System.out.println("Загруженные операции за дату: " + operDay.getOperDate());
                    operDto.forEach(System.out::println);
                }

                //2. вывод отмененных операций
                if (checkId == 2) {
                    var bp = protocols.stream().filter(b -> b.getBusinessProcessEntity().getSysName().equals(OPERATIONS_CANCEL_SYS_NAME)).findFirst();

                    if (bp.isPresent()) {
                        var info = protocolInfoService.getByBusinessProcessByOperDate(bp.get());
                        System.out.println("Отмененные операции за дату: " + operDay.getOperDate());
                        info.getOperationDtoList().forEach(System.out::println);
                    }
                }

                //3. вывод исполненных операций
                if (checkId == 3) {
                    var bp = protocols.stream().filter(b -> b.getBusinessProcessEntity().getSysName().equals(OPERATIONS_EXECUTION_SYS_NAME)).findFirst();

                    if (bp.isPresent()) {
                        var info = protocolInfoService.getByBusinessProcessByOperDate(bp.get());
                        System.out.println("Исполненные операции за дату: " + operDay.getOperDate());
                        info.getOperationDtoList().forEach(System.out::println);
                    }
                }

                //4. вывод переоценки
                if (checkId == 4) {
                    var bp = protocols.stream().filter(b -> b.getBusinessProcessEntity().getSysName().equals(OPERATIONS_CURRENCY_REVAL_SYS_NAME)).findFirst();

                    if (bp.isPresent()) {
                        var info = protocolInfoService.getByBusinessProcessByOperDate(bp.get());
                        System.out.println("Расчитанная переоценка операции за дату: " + operDay.getOperDate());
                        info.getRevalDtoList().forEach(System.out::println);
                    }
                }

            });

        }
    }
}
