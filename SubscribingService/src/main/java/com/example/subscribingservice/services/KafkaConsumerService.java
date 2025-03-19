package com.example.subscribingservice.services;

import org.example.dto.BookStatusMessage;

public interface KafkaConsumerService {

  void consume(BookStatusMessage message);
}
