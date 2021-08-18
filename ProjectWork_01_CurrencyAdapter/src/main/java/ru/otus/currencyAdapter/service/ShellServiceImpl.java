package ru.otus.currencyAdapter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.currencyAdapter.feign.CurrencyLayer;

import java.io.IOException;

@ShellComponent
@RequiredArgsConstructor
public class ShellServiceImpl {
    private final CurrencyLayer currencyLayer;
    private final RateTransformService rateTransformService;

    @ShellMethod(value = "Curr get", key = {"tt"})
    public void api() throws IOException {
        var rates = currencyLayer.getRatesByDate("2018-09-01");

        System.out.println("____________gg_______ " + rates);

        var ratesDto = rateTransformService.layerTransformToDto(rates, 1);

        System.out.println("___________rates dto_____________");
        ratesDto.forEach(System.out::println);


    }
}
