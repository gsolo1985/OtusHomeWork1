//package ru.otus.operations.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.shell.standard.ShellComponent;
//import org.springframework.shell.standard.ShellMethod;
//import ru.otus.operations.publish.newoperday.DateMessage;
//import ru.otus.operations.publish.newoperday.DateMessageOutPublishGateway;
//import ru.otus.operations.publish.operationreval.OperationRevalDto;
//import ru.otus.operations.publish.operationreval.OperationRevalDtoList;
//import ru.otus.operations.publish.operationreval.OperationRevalOutPublishGateway;
//import ru.otus.operations.repository.OperDateRepository;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collections;
//
//@ShellComponent
//@RequiredArgsConstructor
//public class ShellServiceImpl {
//    private final OperDateRepository repository;
//    private final OperDateService operDateService;
//    private final OperationService operationService;
//    private final DateMessageOutPublishGateway dateMessageOutPublishGateway;
//    private final OperationRevalOutPublishGateway operationRevalOutPublishGateway;
//
//    @ShellMethod(value = "oper data", key = {"tt"})
//    public void date() throws IOException {
//        var newDate = operDateService.openOperDay();
//
//        var dto1 = OperationRevalDto.builder()
//                .operationId(1L)
//                .currencyName("RRR")
//                .build();
//
//        var dto2 = OperationRevalDto.builder()
//                .operationId(2L)
//                .currencyName("SSS")
//                .build();
//
//        OperationRevalDtoList list = new OperationRevalDtoList(Arrays.asList(dto1, dto2));
//
//        operationRevalOutPublishGateway.publish(list);

//        dateMessageOutPublishGateway.publish(DateMessage.builder() // отправляем в кафку сообщение, что открыт новый опер. день
//                .date(newDate.getOperDate())
//                .build());
//
//        var deals = operationService.generateByDate(newDate);
//
//        deals.forEach(d -> {
//            System.out.println("----------NEW DEAL----------");
//            System.out.println(d);
//            System.out.println("__________________________");
//        });
//
//        operDateService.closeOperDay(newDate);
//        var newDate2 = operDateService.openOperDay();
//
//        var cancelDeals = operationService.cancelByPlanDate(newDate2);
//
//        cancelDeals.forEach(d -> {
//            System.out.println("----------CANCEL DEAL----------");
//            System.out.println(d);
//            System.out.println("__________________________");
//        });
//
//        var revalDto = operationService.getOperationForRevalByDate(newDate);
//
//        revalDto.forEach(d -> {
//            System.out.println("----------FOR REVAL----------");
//            System.out.println(d);
//            System.out.println("__________________________");
//        });

//        operDateService.closeOperDay(newDate2);
//        var newDate3 = operDateService.openOperDay();
//
//        var execDeals = operationService.execByPlanDate(newDate3);
//
//        execDeals.forEach(d -> {
//            System.out.println("----------EXEC DEAL----------");
//            System.out.println(d);
//            System.out.println("__________________________");
//        });
//    }
//}
