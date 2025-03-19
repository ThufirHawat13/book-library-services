package com.example.booklibraryv2.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.booklibraryv2.dto.bookDTO.BookRequest;
import com.example.booklibraryv2.dto.bookDTO.BookResponse;
import com.example.booklibraryv2.entities.Book;
import com.example.booklibraryv2.mappers.BookMapperImpl;
import com.example.booklibraryv2.services.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class, useDefaultFilters = false)
@Import(value = {BookControllerV1.class, CustomExceptionHandler.class, BookMapperImpl.class})
@ExtendWith(MockitoExtension.class)
class BookControllerV1Test {

  @Autowired
  private MockMvc mvc;
  @MockitoBean
  private BookService bookService;
  private final String ENDPOINT = "/api/v1/books";


  @Test
  void getAllShouldReturnBookDtoList() throws Exception {
    when(bookService.getAll())
        .thenReturn(List.of(getTestBook()));

    mvc.perform(get(ENDPOINT)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("1"))
        .andExpect(jsonPath("$[0].name").value("Book"))
        .andExpect(jsonPath("$[0].author").value("Author"))
        .andExpect(jsonPath("$[0].holder").isEmpty())
        .andExpect(jsonPath("$[0].yearOfWriting").value("1111"));

    verify(bookService, times(1))
        .getAll();
  }

  @Test
  void getByIdShouldReturnBookDto() throws Exception {
    when(bookService.findById(1L))
        .thenReturn(getTestBook());

    mvc.perform(get(ENDPOINT + "/1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.name").value("Book"))
        .andExpect(jsonPath("$.author").value("Author"))
        .andExpect(jsonPath("$.holder").isEmpty())
        .andExpect(jsonPath("$.yearOfWriting")
            .value("1111"));

    verify(bookService, times(1))
        .findById(1L);
  }

  @Test
  void findByNameContainsShouldReturnBookDtoList() throws Exception {
    when(bookService.findByNameContains("Book"))
        .thenReturn(List.of(getTestBook()));

    mvc.perform(get(ENDPOINT + "/find/Book")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id").value("1"))
        .andExpect(jsonPath("$[0].name").value("Book"))
        .andExpect(jsonPath("$[0].author").value("Author"))
        .andExpect(jsonPath("$[0].holder").isEmpty())
        .andExpect(jsonPath("$[0].yearOfWriting").value("1111"));

    verify(bookService, times(1))
        .findByNameContains("Book");
  }

  @Test
  void createShouldReturnBookDTO() throws Exception {
    Book bookForSave = getTestBook();
    bookForSave.setId(null);

    when(bookService.save(bookForSave))
        .thenReturn(getTestBook());

    mvc.perform(post(ENDPOINT)
            .content(asJsonString(getTestCreateBookDTO()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.name").value("Book"))
        .andExpect(jsonPath("$.author").value("Author"))
        .andExpect(jsonPath("$.holder").isEmpty())
        .andExpect(jsonPath("$.yearOfWriting").value("1111"));

    verify(bookService, times(1))
        .save(bookForSave);
  }

  @Test
  void createShouldReturnBadRequestWhenFieldsAreEmpty() throws Exception {
    BookRequest notValidBookRequest = BookRequest.builder()
        .name("")
        .author("")
        .build();

    mvc.perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(notValidBookRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name")
            .value("Name shouldn't be empty!"))
        .andExpect(jsonPath("$.author")
            .value("Author designation shouldn't be empty!"));

    verify(bookService, times(0))
        .save(any());
  }

  @Test
  void createShouldReturnBadRequestWhenFieldsAreBreakingMaximumLength() throws Exception {
    StringBuilder maxLengthPlus1Symbols = new StringBuilder();
    IntStream.rangeClosed(0, 200)
        .forEach(maxLengthPlus1Symbols::append);

    BookRequest notValidBookRequest = BookRequest.builder()
        .name(maxLengthPlus1Symbols.toString())
        .author(maxLengthPlus1Symbols.toString())
        .yearOfWriting(1111)
        .build();

    mvc.perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(notValidBookRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name")
            .value("Length shouldn't be greater than 200!"))
        .andExpect(jsonPath("$.author")
            .value("Length shouldn't be greater than 200!"));

    verify(bookService, times(0))
        .save(any());
  }

  @Test
  void createShouldReturnBadRequestWhenYearOfBirthBreakingMinValue()
      throws Exception {
    BookRequest notValidBookRequest = getTestCreateBookDTO();
    notValidBookRequest.setYearOfWriting(-1);

    mvc.perform(post(ENDPOINT)
            .content(asJsonString(notValidBookRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.yearOfWriting")
            .value("Year of writing shouldn't be lower than 0!"));

    verify(bookService, times(0))
        .save(any());
  }

  @Test
  void createShouldReturnBadRequestWhenYearOfBirthBreakingMaxValue()
      throws Exception {
    BookRequest notValidBookRequest = getTestCreateBookDTO();
    notValidBookRequest.setYearOfWriting(3000);

    mvc.perform(post(ENDPOINT)
            .content(asJsonString(notValidBookRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.yearOfWriting")
            .value("Year of writing shouldn't be greater than 2024!"));

    verify(bookService, times(0))
        .save(any());
  }

  @Test
  void updateShouldReturnUpdatedBook() throws Exception {
    when(bookService.update(1L, getTestUpdateBookDTO()))
        .thenReturn(getTestBook());

    mvc.perform(patch(ENDPOINT + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(getTestBookDTO())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.name").value("Book"))
        .andExpect(jsonPath("$.author").value("Author"))
        .andExpect(jsonPath("$.holder").isEmpty())
        .andExpect(jsonPath("$.yearOfWriting").value("1111"));

    verify(bookService, times(1))
        .update(1L, getTestUpdateBookDTO());
  }

  @Test
  void updateShouldReturnBadRequestWhenFieldsAreBreakingMaxLength() throws Exception {
    StringBuilder maxValidLengthPlus1Symbols = new StringBuilder();
    IntStream
        .rangeClosed(0, 200)
        .forEach(maxValidLengthPlus1Symbols::append);

    BookRequest notValidUpdateBookDTO = BookRequest.builder()
        .name(maxValidLengthPlus1Symbols.toString())
        .author(maxValidLengthPlus1Symbols.toString())
        .build();

    mvc.perform(patch(ENDPOINT + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(notValidUpdateBookDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name")
            .value("Length shouldn't be greater than 200!"))
        .andExpect(jsonPath("$.author")
            .value("Length shouldn't be greater than 200!"));

    verify(bookService, times(0))
        .update(anyLong(), any());
  }

  @Test
  void updateShouldReturnBadRequestWhenYearOfWritingBreakingMaxValue() throws Exception {
    BookRequest notValidUpdateBookDTO =
        BookRequest.builder()
            .yearOfWriting(3000)
            .build();

    mvc.perform(patch(ENDPOINT + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(notValidUpdateBookDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.yearOfWriting")
            .value("Year of writing shouldn't be greater than 2024!"));

    verify(bookService, times(0))
        .update(anyLong(), any());
  }

  @Test
  void updateShouldReturnBadRequestWhenYearOfBirthBreakingMinValue() throws Exception {
    BookRequest notValidUpdateBookDTO =
        BookRequest.builder()
            .yearOfWriting(-1)
            .build();

    mvc.perform(patch(ENDPOINT + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(notValidUpdateBookDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.yearOfWriting")
            .value("Year of writing shouldn't be lower than 0!"));

    verify(bookService, times(0))
        .update(anyLong(), any());
  }

  @Test
  void deleteShouldDeleteSuccessful() throws Exception {
    mvc.perform(delete(ENDPOINT + "/1"))
        .andExpect(status().isNoContent());

    verify(bookService, times(1))
        .delete(1L);
  }

  private Book getTestBook() {
    return Book.builder()
        .id(1L)
        .name("Book")
        .author("Author")
        .holder(null)
        .yearOfWriting(1111)
        .build();
  }

  private BookResponse getTestBookDTO() {
    return BookResponse.builder()
        .id(1L)
        .name("Book")
        .author("Author")
        .holder(null)
        .yearOfWriting(1111)
        .build();
  }

  private BookRequest getTestCreateBookDTO() {
    return BookRequest.builder()
        .name("Book")
        .author("Author")
        .yearOfWriting(1111)
        .build();
  }

  private BookRequest getTestUpdateBookDTO() {
    return BookRequest.builder()
        .name("Book")
        .author("Author")
        .yearOfWriting(1111)
        .build();
  }

  private String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}