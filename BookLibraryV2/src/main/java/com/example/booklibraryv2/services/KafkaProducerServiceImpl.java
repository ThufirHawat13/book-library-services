package com.example.booklibraryv2.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.BookStatusMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

  private final KafkaTemplate<String, BookStatusMessage> kafkaTemplate;


  @Override
  public void send(String topicName, BookStatusMessage message) {
    var future = kafkaTemplate.send(topicName, message);

    future.whenComplete((result, exception) -> {
      if (exception != null) {
        future.completeExceptionally(exception);
      } else {
        future.complete(result);
      }
      log.info("Message '{}' sended to Kafka topic '{}'",
          message, topicName);
    });
  }
}
