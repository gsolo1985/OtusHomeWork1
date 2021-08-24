package ru.otus.operations.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.operations.domain.RevalEntity;
import ru.otus.operations.service.BusinessProcessByOperDateService;
import ru.otus.operations.service.OperDateService;
import ru.otus.operations.service.RevalService;
import ru.otus.operations.service.processing.ProcessingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevalOperationListenerImpl implements RevalOperationListener {
    private final BusinessProcessByOperDateService businessProcessByOperDateService;
    private final OperDateService operDateService;
    private final ProcessingService processingService;
    private final RevalService revalService;

    @KafkaListener(
            topics = "${deals.kafka-topic-name.calc-reval-out}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")

    @Override
    public void consume(RevalOperationList msg) throws IOException {
        var revalDtoList = msg.getRevalOperationList();

        if (revalDtoList != null && revalDtoList.size() > 0) {
            List<RevalEntity> revalEntities = new ArrayList<>();

            revalDtoList.forEach(r -> {
                revalEntities.add(revalService.dtoTransformToEntity(r));
            });

            revalEntities.forEach(revalService::save);

            var operDay = operDateService.getOperDay();

            if (operDay.isPresent()) {
                boolean checkEndDay = businessProcessByOperDateService.checkProcessChainEndByOperDate(operDay.get());

                if (checkEndDay) {
                    processingService.stopProcessingOperDay();
                }
            }
        }
        
    }
}
