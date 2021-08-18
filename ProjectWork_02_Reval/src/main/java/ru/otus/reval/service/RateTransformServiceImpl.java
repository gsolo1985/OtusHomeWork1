package ru.otus.reval.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.reval.consumer.CurrencyRateDto;
import ru.otus.reval.domain.CurrencyEntity;
import ru.otus.reval.domain.CurrencyRateEntity;
import ru.otus.reval.repository.CurrencyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RateTransformServiceImpl implements RateTransformService {
    private final CurrencyRepository currencyRepository;

    @Override
    public void setCurrencyEntityToListRateDto(List<CurrencyRateDto> rates) {
        for (var currencyRateDto : rates) {
            String buyName = currencyRateDto.getCurrencyPair().substring(0, 3);
            String sellName = currencyRateDto.getCurrencyPair().substring(3, 6);

            var buyCurrency = currencyRepository.findByName(buyName);
            var sellCurrency = currencyRepository.findByName(sellName);

            buyCurrency.ifPresentOrElse(
                    currencyRateDto::setBuyCurrency,
                    () -> {
                        var currBuySave = currencyRepository.save(CurrencyEntity.builder()
                                .name(buyName)
                                .build());
                        currencyRateDto.setBuyCurrency(currBuySave);
                    });

            sellCurrency.ifPresentOrElse(
                    currencyRateDto::setSellCurrency,
                    () -> {
                        var currSellSave = currencyRepository.save(CurrencyEntity.builder()
                                .name(sellName)
                                .build());
                        currencyRateDto.setSellCurrency(currSellSave);
                    });
        }
    }

    @Override
    public List<CurrencyRateEntity> getCurrencyRateEntityFromRateDto(List<CurrencyRateDto> rates) {
        List<CurrencyRateEntity> result = new ArrayList<>();

        for (var rateDto : rates) {
          result.add(CurrencyRateEntity.builder()
                  .currencyBuy(rateDto.getBuyCurrency())
                  .currencySell(rateDto.getSellCurrency())
                  .date(LocalDate.parse(rateDto.getDate()))
                  .value(rateDto.getRate())
                  .build());
        }

        return result;
    }
}
