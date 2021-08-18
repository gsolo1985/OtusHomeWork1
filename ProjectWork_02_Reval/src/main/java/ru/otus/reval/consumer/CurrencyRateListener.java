package ru.otus.reval.consumer;

public interface CurrencyRateListener {
    void consume(CurrencyRateDto msg);
}
