//package ru.otus.operations.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.shell.standard.ShellComponent;
//import org.springframework.shell.standard.ShellMethod;
//import ru.otus.operations.repository.OperDateRepository;
//
//import java.io.IOException;
//
//@ShellComponent
//@RequiredArgsConstructor
//public class ShellServiceImpl {
//    private final OperDateRepository repository;
//    private final OperDateService operDateService;
//
//    @ShellMethod(value = "oper data", key = {"tt"})
//    public void date() throws IOException {
//        var operDates = repository.findAll();
//
//        System.out.println("all1 = " + operDates);
//
//        operDateService.fillOperDate();
//
//        var operDates2 = repository.findAll();
//
//        System.out.println("all2 = " + operDates2);
////
////        var date = repository.findTop1ByCloseFlagOrderByOperDate(false);
////
////        System.out.println("___________");
////        System.out.println(date);
////
////        System.out.println("RRRRRRRR");
////        boolean ex = repository.existsByIdGreaterThan(0);
////
////        System.out.println(ex);
////
////        var date = repository.findTop1ByIdGreaterThanOrderByOperDateDesc(0);
////
////        System.out.println("FFFF = " + date);
//    }
//}
