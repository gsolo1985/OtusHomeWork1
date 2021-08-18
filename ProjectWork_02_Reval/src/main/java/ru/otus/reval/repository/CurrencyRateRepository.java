package ru.otus.reval.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.reval.domain.CurrencyEntity;
import ru.otus.reval.domain.CurrencyRateEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRateRepository extends CrudRepository<CurrencyRateEntity, Long> {
    @EntityGraph("currency-rate-graph")
    List<CurrencyRateEntity> findByDate(LocalDate date);

    @Override
    @EntityGraph("currency-rate-graph")
    List<CurrencyRateEntity> findAll();

    @EntityGraph("currency-rate-graph")
    Optional<CurrencyRateEntity> findByDateAndCurrencyBuyAndCurrencySell(LocalDate date, CurrencyEntity currencyBuy, CurrencyEntity currencySell);
}
