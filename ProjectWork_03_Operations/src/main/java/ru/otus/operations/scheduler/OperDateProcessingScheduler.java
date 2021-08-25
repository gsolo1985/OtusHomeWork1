package ru.otus.operations.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.operations.repository.BusinessProcessByOperDateRepository;
import ru.otus.operations.service.processing.ProcessingService;

@Service
@RequiredArgsConstructor
public class OperDateProcessingScheduler {
    private final BusinessProcessByOperDateRepository businessProcessByOperDateRepository;
    private final ProcessingService processingService;

    @Scheduled(fixedDelayString = "${schedulerTask.operDateProcessing.interval}")
    public void startDayProcessing() {
        processingService.startProcessingOperDay();
    }
}
