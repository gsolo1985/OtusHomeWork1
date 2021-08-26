package ru.otus.operations.controller;

import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.operations.model.ProtocolDto;
import ru.otus.operations.model.ProtocolInfoDto;
import ru.otus.operations.service.ProtocolService;
import ru.otus.operations.service.ProtocolInfoService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController("ru.otus.operations.controller.ProtocolController")
@Generated
@AllArgsConstructor
public class ProtocolController {
    private final ProtocolService service;
    private final ProtocolInfoService protocolService;

    @GetMapping("/protocols/operationDate")
    public List<ProtocolDto> getProtocolsByOperationDate(
            @Valid
            @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
            @RequestParam(name = "OperationDate") LocalDate operationDate) {
        List<ProtocolDto> result = new ArrayList<>();
        var protocols = service.findByOperDate(operationDate);

        protocols.forEach(p -> {
            result.add(service.entityToDto(p));
        });
        return result;
    }

    @GetMapping("/protocols/info")
    public ProtocolInfoDto getProtocolInfoByOperationDate(
            @Valid
            @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
            @RequestParam(name = "OperationDate", required = true) LocalDate operationDate,
            @Valid
            @RequestParam(name = "BusinessProcessId", required = true) long id) {
        var protocols = service.findByOperDate(operationDate);
        var bp = protocols.stream().filter(b -> b.getBusinessProcessEntity().getBusinessProcessId().equals(id)).findFirst();

        if (bp.isPresent()) {
            return protocolService.getByBusinessProcessByOperDate(bp.get());
        }
        else return new ProtocolInfoDto();
    }
}
