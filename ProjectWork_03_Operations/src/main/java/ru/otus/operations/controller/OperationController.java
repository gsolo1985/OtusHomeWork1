package ru.otus.operations.controller;

import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.operations.model.OperationDto;
import ru.otus.operations.service.OperationSaveService;
import ru.otus.operations.service.OperationService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController("ru.otus.operations.controller.OperationController")
@Generated
@AllArgsConstructor
public class OperationController {
    private final OperationService service;
    private final OperationSaveService operationSaveService;

    @GetMapping("/operations/operationDate")
    public List<OperationDto> getOperationsByOperationDate(
            @Valid
            @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
            @RequestParam(name = "OperationDate") LocalDate operationDate) {
        var operations = service.findByOperationDate(operationDate);
        List<OperationDto> result = new ArrayList<>();

        operations.forEach(o -> {
            result.add(service.entityToDto(o));
        });

        return result;
    }

    @GetMapping("/operations/{id}")
    public OperationDto getOperationById(
            @Valid
            @PathVariable(name = "id") Long id) {
        return service.entityToDto(service.findById(id).orElse(null));
    }

    @PostMapping("/operations/operation")
    public OperationDto addOperation(
            @Valid
            @RequestBody OperationDto operationDto) {
        return operationSaveService.saveDto(operationDto);
    }

    @PutMapping("/operations/operation")
    public OperationDto updateOperation(
            @Valid
            @RequestBody OperationDto operationDto) {
        return operationSaveService.saveDto(operationDto);
    }

    @DeleteMapping("/operations/{id}")
    public ResponseEntity<Void> deleteCurrency(
            @Valid
            @PathVariable(name = "id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
