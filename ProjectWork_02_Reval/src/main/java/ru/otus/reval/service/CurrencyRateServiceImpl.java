package ru.otus.reval.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.reval.domain.CurrencyEntity;
import ru.otus.reval.domain.CurrencyRateEntity;
import ru.otus.reval.exception.CurrencyRateNotValidException;
import ru.otus.reval.model.CurrencyRateGet;
import ru.otus.reval.repository.CurrencyRateRepository;
import ru.otus.reval.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {
    private final CurrencyRateRepository currencyRateRepository;
    private final CurrencyRepository currencyRepository;

    private static final String INSERT_ERROR = "New currency rate insert error: ";

    /**
     * Сохранить сущность котировка
     * @param currencyRateEntity - entity-объект котировка
     * @return - сохраненный объект
     */
    @Override
    @Transactional
    public CurrencyRateEntity save(CurrencyRateEntity currencyRateEntity) {
        if (currencyRateEntity == null) {
            throw new CurrencyRateNotValidException(INSERT_ERROR + "currencyRate isn't defined.");
        }

        BigDecimal value = currencyRateEntity.getValue();
        LocalDate date = currencyRateEntity.getDate();
        CurrencyEntity buy = currencyRateEntity.getCurrencyBuy();
        CurrencyEntity sell = currencyRateEntity.getCurrencySell();

        if (value == null || value.equals(BigDecimal.valueOf(0))) {
            throw new CurrencyRateNotValidException(INSERT_ERROR + "you must set a rate value.");
        }

        if (date == null) {
            throw new CurrencyRateNotValidException(INSERT_ERROR + "you must set a date.");
        }

        if (buy == null || sell ==null) {
            throw new CurrencyRateNotValidException(INSERT_ERROR + "you must set a buy and sell currencies.");
        }

        var rateExist = currencyRateRepository.findByDateAndCurrencyBuyAndCurrencySell(currencyRateEntity.getDate(), currencyRateEntity.getCurrencyBuy(), currencyRateEntity.getCurrencySell());

        rateExist.ifPresent(r ->
                currencyRateEntity.setId(r.getId()));

        return currencyRateRepository.save(currencyRateEntity);
    }

    /**
     * Сохранить котировку из dto-объекта
     * @param currencyRateGet - Dto
     * @return - сохраненный объект
     */
    @Override
    @Transactional
    public CurrencyRateEntity saveDto(CurrencyRateGet currencyRateGet) {
        String buyCurrencyName = currencyRateGet.getBuyCurrencyName();
        String sellCurrencyName = currencyRateGet.getSellCurrencyName();

        if (buyCurrencyName == null && sellCurrencyName == null) {
            throw new CurrencyRateNotValidException(INSERT_ERROR + "you must set a buy and sell currencies.");
        }

        var buyCurrency = currencyRepository.findByName(buyCurrencyName);
        var sellCurrency = currencyRepository.findByName(sellCurrencyName);

        var saveRate = CurrencyRateEntity.builder()
                .date(currencyRateGet.getDate())
                .value(currencyRateGet.getRate())
                .build();

        buyCurrency.ifPresentOrElse(
                saveRate::setCurrencyBuy,
                () -> {
                    var currBuySave = currencyRepository.save(CurrencyEntity.builder()
                            .name(buyCurrencyName)
                            .build());
                    saveRate.setCurrencyBuy(currBuySave);
                });

        sellCurrency.ifPresentOrElse(
                saveRate::setCurrencySell,
                () -> {
                    var currSellSave = currencyRepository.save(CurrencyEntity.builder()
                            .name(sellCurrencyName)
                            .build());
                    saveRate.setCurrencyBuy(currSellSave);
                });

        return save(saveRate);
    }

    /**
     * Получить dto-объект котировки по id
     * @param id - идентификатор котировки
     * @return - котировка
     */
    @Override
    @Transactional(readOnly = true)
    public CurrencyRateGet getById(long id) {
        var currencyRateEntity = currencyRateRepository.findById(id);

        if (currencyRateEntity.isPresent()) {
            return transformToDto(currencyRateEntity.get());
        }
        return new CurrencyRateGet();
    }

    /**
     * Получить курс по валютной паре за дату
     * @param currencyPair - валютная пара в формате "USDRUB"
     * @param date - дата
     * @return - котировка
     */
    @Override
    @Transactional(readOnly = true)
    public CurrencyRateGet getByCurrencyPairAndDate(String currencyPair, LocalDate date) {
        if (currencyPair != null && date != null && currencyPair.length() == 6) {
            String buyCurrencyName = currencyPair.substring(0, 3);
            String sellCurrencyName = currencyPair.substring(3, 6);

            var buyCurrency = currencyRepository.findByName(buyCurrencyName);
            var sellCurrency = currencyRepository.findByName(sellCurrencyName);

            if (buyCurrency.isPresent() && sellCurrency.isPresent()) {
                var rate = currencyRateRepository.findByDateAndCurrencyBuyAndCurrencySell(date, buyCurrency.get(), sellCurrency.get());


                if (rate.isPresent()) {
                    return transformToDto(rate.get());
                }
            }
        }

        return new CurrencyRateGet();
    }

    /**
     * Преобразование entity в dto
     * @param currencyRateEntity - entity
     * @return - dto
     */
    @Override
    public CurrencyRateGet transformToDto(CurrencyRateEntity currencyRateEntity) {
        if (currencyRateEntity != null) {
            return CurrencyRateGet.builder()
                    .buyCurrencyName(currencyRateEntity.getCurrencyBuy().getName())
                    .sellCurrencyName(currencyRateEntity.getCurrencySell().getName())
                    .buyCurrencyId(currencyRateEntity.getCurrencyBuy().getId())
                    .sellCurrencyId(currencyRateEntity.getCurrencySell().getId())
                    .date(currencyRateEntity.getDate())
                    .rate(currencyRateEntity.getValue())
                    .id(currencyRateEntity.getId())
                    .build();
        }
        return new CurrencyRateGet();
    }

    /**
     * Удаление по id
     * @param id - идентификатор
     */
    @Override
    @Transactional
    public void deleteById(long id) {
        currencyRateRepository.deleteById(id);
    }
}
