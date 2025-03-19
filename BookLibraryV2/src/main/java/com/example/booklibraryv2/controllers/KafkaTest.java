package com.example.booklibraryv2.controllers;

import com.example.booklibraryv2.services.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.example.dto.BookStatusMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class KafkaTest {

  private final KafkaProducerService kafkaProducerService;

  @GetMapping
  public void test() {
    kafkaProducerService.send("book-status",
        BookStatusMessage.builder()
            .id(1L)
            .bookName("TEST")
            .status("TEST")
            .build());
  }

}
