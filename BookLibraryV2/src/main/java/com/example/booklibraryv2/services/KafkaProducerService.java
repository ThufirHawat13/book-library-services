package com.example.booklibraryv2.services;

import org.example.dto.BookStatusMessage;

public interface KafkaProducerService {

  void send(String topicName, BookStatusMessage message);
}
