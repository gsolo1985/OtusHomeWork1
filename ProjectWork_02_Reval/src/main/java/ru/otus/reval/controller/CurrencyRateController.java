package ru.otus.reval.controller;

import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.otus.reval.model.CurrencyRateGet;
import ru.otus.reval.service.CurrencyRateService;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController("ru.otus.reval.controller.CurrencyRateController")
@Generated
@AllArgsConstructor
public class CurrencyRateController {
    private CurrencyRateService service;

    @GetMapping("/currencyRates/{id}")
    public CurrencyRateGet getRateById(
            @Valid
            @PathVariable(name = "id") Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/currencyRates/{id}")
    @Transactional
    public ResponseEntity<Void> deleteCurrencyRate(
            @Valid
            @PathVariable(name = "id") Long id) {
        CurrencyRateGet rate = service.getById(id);
        service.deleteById(rate.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/currencyRate/currencyPairRateByDate")
    public CurrencyRateGet getRateByCurrencyPairAndDate(
            @Valid
            @RequestParam(name = "currencyPair") String currencyPair,
            @Valid
            @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
            @RequestParam(name = "date")
                    LocalDate date) {
        return service.getByCurrencyPairAndDate(currencyPair, date);
    }

    @PostMapping("/currencyRates/currencyRate")
    public CurrencyRateGet saveCurrencyRate(
            @Valid
            @RequestBody CurrencyRateGet currencyRateGet) {
        return service.transformToDto(service.saveDto(currencyRateGet));
    }
}
