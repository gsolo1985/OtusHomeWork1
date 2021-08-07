package ru.otus.spring.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.spring.dto.BookDto;

@Service
@RequiredArgsConstructor
public class BookGetListenerImpl implements BookGetListener {
    @KafkaListener(
            topics = "${library.kafka-topic-name.get-out-book}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    @Override
    public void consume(BookDto book) {
        System.out.println("Получена книга:");
        System.out.println("Название - " + book.getTitle());
        System.out.println("Автор - " + book.getAuthor().getName());
        System.out.println("Жанр - " + book.getGenre().getName());
        System.out.println("________________________");
    }
}