package ru.otus.reval.controller;

import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.reval.domain.CurrencyEntity;
import ru.otus.reval.exception.NotFoundException;
import ru.otus.reval.model.CurrencyGet;
import ru.otus.reval.repository.CurrencyRepository;
import ru.otus.reval.service.CurrencyService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController("ru.otus.reval.controller.CurrencyController")
@Generated
@AllArgsConstructor
public class CurrencyController {
    private final CurrencyService service;
    private final CurrencyRepository repository;


    @GetMapping("/currencies/{name}")
    public CurrencyGet getCurrencyByName(
            @Valid
            @PathVariable(name = "name") String name) {
        var currencyEntity = service.getByName(name);

        if (currencyEntity.isPresent()) {
            return service.transformToDto(currencyEntity.get());
        }
        return new CurrencyGet();
    }

    @GetMapping("/currencies/")
    public List<CurrencyGet> getCurrencies() {
        var currencies = service.findAll();
        List<CurrencyGet> result = new ArrayList<>();

        currencies.forEach(c -> result.add(service.transformToDto(c)));

        return result;
    }

    @DeleteMapping("/currencies/{id}")
    public ResponseEntity<Void> deleteCurrency(
            @Valid
            @PathVariable(name = "id") Long id) {
        CurrencyEntity currencyEntity = service.getById(id).orElseThrow(NotFoundException::new);
        service.delete(currencyEntity);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/currencies/currency")
    public CurrencyGet saveCurrency(
            @Valid
            @RequestBody CurrencyGet currencyGet) {
        var currency = service.save(service.transformToEntity(currencyGet));
        return service.transformToDto(currency);
    }

    @PutMapping("/currencies/currency")
    public CurrencyGet updateCurrency(
            @Valid
            @RequestBody CurrencyGet currencyGet) {
        var currencyUpdated = service.save(service.transformToEntity(currencyGet));
        return service.transformToDto(currencyUpdated);
    }
}
