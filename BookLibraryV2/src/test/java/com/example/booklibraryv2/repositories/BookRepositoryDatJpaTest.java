package com.example.booklibraryv2.repositories;

import com.example.booklibraryv2.entities.Book;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2,
    replace = Replace.AUTO_CONFIGURED)
class BookRepositoryDatJpaTest {

  @Autowired
  private BookRepository bookRepository;


  @BeforeEach
  void setUp() {
    bookRepository.saveAll(List.of(
        getTestBookWithCustomName("test1"),
        getTestBookWithCustomName("test2"),
        getTestBookWithCustomName("book"))
    );
  }

  @AfterEach
  void tearDown() {
    bookRepository.deleteAll();
  }

  @Test
  void findByNameContainsShouldReturnBookList() {
    Assertions.assertEquals(List.of(
        getTestBookWithCustomName("test1"),
        getTestBookWithCustomName("test2")),
        bookRepository.findByNameContains("test")
    );
  }

  @Test
  void findByNameContainsShouldReturnEmptyList() {
    Assertions.assertEquals(List.of(),
        bookRepository.findByNameContains("list"));
  }

  private Book getTestBookWithCustomName(String name) {
    return Book.builder()
        .name(name)
        .author("test")
        .yearOfWriting(1995)
        .build();
  }
}