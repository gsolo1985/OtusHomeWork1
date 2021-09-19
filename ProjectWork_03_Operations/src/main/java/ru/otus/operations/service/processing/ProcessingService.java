package ru.otus.operations.service.processing;

public interface ProcessingService {
    /**
     * Начать обработку операционного дня:
     * Открывается новый операционный день и выполняются настроенные бизнес-процессы.
     */
    void startProcessingOperDay();

    /**
     * Завершение обработки операционного дня. Закрытие операционного дня.
     */
    void stopProcessingOperDay();

}
