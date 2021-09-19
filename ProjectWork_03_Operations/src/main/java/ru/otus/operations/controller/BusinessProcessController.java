package ru.otus.operations.controller;

import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.operations.model.BusinessProcessDto;
import ru.otus.operations.service.BusinessProcessService;

import java.util.ArrayList;
import java.util.List;

@RestController("ru.otus.operations.controller.BusinessProcessController")
@Generated
@AllArgsConstructor
public class BusinessProcessController {
    private final BusinessProcessService service;

    @GetMapping("/businessprocesses")
    public List<BusinessProcessDto> getBusinessProcess() {
        List<BusinessProcessDto> result = new ArrayList<>();
        var bpList = service.findAll(Sort.by("OrderType"));

        bpList.forEach(bp -> {
            result.add(service.entityToDto(bp));
        });

        return result;
    }
}
