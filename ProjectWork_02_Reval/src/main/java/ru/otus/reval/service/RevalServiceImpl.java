package ru.otus.reval.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.reval.consumer.reval.OperationRevalDto;
import ru.otus.reval.domain.CurrencyEntity;
import ru.otus.reval.model.RevalOperation;
import ru.otus.reval.repository.CurrencyRateRepository;
import ru.otus.reval.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RevalServiceImpl implements RevalService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyRateRepository currencyRateRepository;

    @Value(value = "${nationalCurrency.name}")
    private String nationalCurrencyName;
    @Value(value = "${rateTargetCurrency.name}")
    private String rateTargetCurrencyName;


    /**
     * Расчитать переоценку по операции
     *
     * @param revalOperation - объект с операцией
     */
    @Override
    public void calcReval(RevalOperation revalOperation) {
        System.out.println("************CALC_REVAL****************");
        final CurrencyEntity buyCurrency = currencyRepository.findByName(rateTargetCurrencyName).orElse(null);
        final CurrencyEntity natCurrency = currencyRepository.findByName(nationalCurrencyName).orElse(null);
        var currencyName = revalOperation.getCurrencyName();
        var date = revalOperation.getRevalDate();
        var operationCurrency = currencyRepository.findByName(currencyName);

        System.out.println("currencyName = " + currencyName);
        System.out.println("date = " + date);
        System.out.println("buyCurrency = " + buyCurrency);
        System.out.println("natCurrency = " + natCurrency);
        System.out.println("operationCurrency.isPresent() = " + operationCurrency.isPresent());

        if (currencyName != null
                && date != null
                && buyCurrency != null
                && natCurrency != null
                && operationCurrency.isPresent()) {
            if (currencyName.equals(nationalCurrencyName)) {
                System.out.println("CR1");
                revalOperation.setRevalAmount(revalOperation.getAmount());
            } else {
                System.out.println("CR2");
                var rateNational = currencyRateRepository.findByDateAndCurrencyBuyAndCurrencySell(date, buyCurrency, natCurrency);
                rateNational.ifPresent(rn -> {
                    System.out.println("CR3");
                    if (currencyName.equals(buyCurrency.getName())) {
                        revalOperation.setRevalAmount(revalOperation.getAmount().multiply(rn.getValue()).setScale(5, RoundingMode.CEILING));
                    } else {
                        System.out.println("CR4");
                        var rate = currencyRateRepository.findByDateAndCurrencyBuyAndCurrencySell(date, buyCurrency, operationCurrency.get());
                        rate.ifPresent(r -> {
                            System.out.println("CR5");
                            var amount = revalOperation.getAmount();
                            var rateOper = r.getValue();
                            var rateNat = rn.getValue();

                            BigDecimal num = amount.divide(rateOper, 10, RoundingMode.CEILING);
                            BigDecimal result = num.multiply(rateNat).setScale(5, RoundingMode.CEILING);

                            System.out.println("CR amount = " + amount);
                            System.out.println("CR rateOper = " + rateOper);
                            System.out.println("CR rateNat = " + rateNat);
                            System.out.println("CR num = " + num);
                            System.out.println("CR result = " + result);

                            revalOperation.setRevalAmount(result);
                        });
                    }
                });
            }
        }
    }

    /**Преобразование объекта OperationRevalDto в RevalOperation
     *
     * @param dto - объект для преобразования
     * @return - преобразованный объект
     */
    @Override
    public RevalOperation transform(OperationRevalDto dto) {
        return RevalOperation.builder()
                .amount(dto.getAmount())
                .revalDate(LocalDate.parse(dto.getRevalDate()))
                .operationId(dto.getOperationId())
                .currencyName(dto.getCurrencyName())
                .build();
    }
}