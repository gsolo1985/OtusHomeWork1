package ru.otus.currencyAdapter.publish;

public interface CurrencyRateByDateOutPublishGateway {
    void publish(CurrencyRateDtoList msg);
}

