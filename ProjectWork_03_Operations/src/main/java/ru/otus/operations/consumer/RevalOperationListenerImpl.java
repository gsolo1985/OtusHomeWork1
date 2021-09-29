package ru.otus.operations.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.operations.service.processing.RevalOperationService;

@Service
@RequiredArgsConstructor
public class RevalOperationListenerImpl implements RevalOperationListener {
    private final RevalOperationService revalOperationService;

    @KafkaListener(
            topics = "${deals.kafka-topic-name.calc-reval-out}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")

    @Override
    public void consume(RevalOperationList msg) {
        revalOperationService.saveCalcReval(msg);
    }
}
