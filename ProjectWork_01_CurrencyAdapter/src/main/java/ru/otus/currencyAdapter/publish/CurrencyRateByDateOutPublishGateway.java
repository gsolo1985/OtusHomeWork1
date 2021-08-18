package ru.otus.currencyAdapter.publish;

public interface CurrencyRateByDateOutPublishGateway {
    void publish(CurrencyRateDto msg);
}

