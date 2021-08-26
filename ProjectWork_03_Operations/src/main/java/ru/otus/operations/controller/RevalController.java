package ru.otus.operations.controller;

import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.operations.model.RevalDto;
import ru.otus.operations.service.OperationService;
import ru.otus.operations.service.RevalService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController("ru.otus.operations.controller.RevalController")
@Generated
@AllArgsConstructor
public class RevalController {
    private final RevalService revalService;
    private final OperationService operationService;

    @GetMapping("/reval/operation")
    public List<RevalDto> getOperationsByOperationDate(
            @Valid
            @RequestParam(name = "operationId") long operationId) {
        List<RevalDto> result = new ArrayList<>();
        var operation = operationService.findById(operationId);

        if (operation.isEmpty())
            return new ArrayList<>();

        var revalList = revalService.findByOperationEntity(operation.get());

        revalList.forEach(r -> result.add(revalService.entityToDto(r)));

        return result;
    }
}
