package ru.otus.operations.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.operations.repository.ProtocolRepository;
import ru.otus.operations.service.processing.ProcessingService;

@Service
@RequiredArgsConstructor
public class OperDateProcessingScheduler {
    private final ProcessingService processingService;

    @Value(value = "${app_mode}")
    private int appMode;

    @Scheduled(fixedDelayString = "${schedulerTask.operDateProcessing.interval}")
    public void startDayProcessing() {
        if (appMode == 0)
            processingService.startProcessingOperDay();
    }
}
