package com.example.subscribingservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.BookStatusMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

  @Override
  @KafkaListener(topics = {"book-status"}, groupId = "group1")
  public void consume(BookStatusMessage message) {
    System.out.println(message);
  }
}
