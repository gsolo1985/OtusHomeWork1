package ru.otus.reval.consumer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig
{
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${deals.kafka-topic-name.currency-rate-by-date-out}")
    private String rateTopicName;

    @Value(value = "${deals.kafka-topic-name.operation-reval-out}")
    private String revalTopicName;

    @Bean
    public NewTopic rateTopic() {
        return TopicBuilder.name(rateTopicName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic revalTopic() {
        return TopicBuilder.name(revalTopicName)
                .partitions(1)
                .replicas(1)
                .build();
    }
}