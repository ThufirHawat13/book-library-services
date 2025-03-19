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

import com.example.booklibraryv2.dto.libraryUserDTO.LibraryUserRequest;
import com.example.booklibraryv2.entities.LibraryUser;
import com.example.booklibraryv2.mappers.LibraryUserMapperImpl;
import com.example.booklibraryv2.services.LibraryUserService;
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
@Import(value = {LibraryUserControllerV1.class, CustomExceptionHandler.class,
    LibraryUserMapperImpl.class})
@ExtendWith(MockitoExtension.class)
class LibraryUserControllerV1Test {

  @Autowired
  private MockMvc mvc;
  @MockitoBean
  private LibraryUserService libraryUserService;
  private final String ENDPOINT = "/api/v1/library-users";


  @Test
  void getAllShouldReturnLibraryUserDtoList() throws Exception {
    when(libraryUserService.getAll())
        .thenReturn(List.of(getTestLibraryUser()));

    mvc.perform(get(ENDPOINT)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Name"))
        .andExpect(jsonPath("$[0].surname").value("Surname"))
        .andExpect(jsonPath("$[0].bookList").isEmpty());

    verify(libraryUserService, times(1))
        .getAll();
  }

  @Test
  void getByIdShouldReturnLibraryUserDto() throws Exception {
    when(libraryUserService.findById(1L))
        .thenReturn(getTestLibraryUser());

    mvc.perform(get(ENDPOINT + "/1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Name"))
        .andExpect(jsonPath("$.surname").value("Surname"))
        .andExpect(jsonPath("$.bookList").isEmpty());

    verify(libraryUserService, times(1))
        .findById(1L);
  }

  @Test
  void findByNameContainsShouldReturnLibraryUserDtoList() throws Exception {
    when(libraryUserService.findByNameContains("Name"))
        .thenReturn(List.of(getTestLibraryUser()));

    mvc.perform(get(ENDPOINT + "/find/Name")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Name"))
        .andExpect(jsonPath("$[0].surname").value("Surname"))
        .andExpect(jsonPath("$[0].bookList").isEmpty());

    verify(libraryUserService, times(1))
        .findByNameContains("Name");
  }

  @Test
  void createShouldReturnSavedLibraryUser() throws Exception {
    LibraryUser libraryUserForSave = getTestLibraryUser();
    libraryUserForSave.setId(null);

    when(libraryUserService.save(libraryUserForSave))
        .thenReturn(getTestLibraryUser());

    mvc.perform(post(ENDPOINT)
            .content(asJsonString(getTestLibraryUserRequestDto()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Name"))
        .andExpect(jsonPath("$.surname").value("Surname"))
        .andExpect(jsonPath("$.bookList").isEmpty());

    verify(libraryUserService, times(1))
        .save(libraryUserForSave);
  }

  @Test
  void createShouldReturnBadRequestWhenFieldsAreEmpty() throws Exception {
    LibraryUserRequest notValidLibraryUserRequest = getTestLibraryUserRequestDto();
    notValidLibraryUserRequest.setName("");
    notValidLibraryUserRequest.setSurname("");

    mvc.perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(notValidLibraryUserRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name")
            .value("Name is not valid!"))
        .andExpect(jsonPath("$.surname")
            .value("Surname is not valid!"));

    verify(libraryUserService, times(0))
        .save(any());
  }

  @Test
  void createShouldReturnBadRequestWhenFieldsAreBreakingMaximumLength() throws Exception {
    var maximumLengthPlus1Words = new StringBuilder("F");

    IntStream.rangeClosed(0, 30)
        .forEach(num -> maximumLengthPlus1Words.append("f"));

    var notValidLibraryUserRequestDTO = LibraryUserRequest.builder()
        .name(maximumLengthPlus1Words.toString())
        .surname(maximumLengthPlus1Words.toString())
        .build();

    mvc.perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(notValidLibraryUserRequestDTO)))
        .andExpect(jsonPath("$.name")
            .value("Length shouldn't be greater than 30!"))
        .andExpect(jsonPath("$.surname")
            .value("Length shouldn't be greater than 30!"));

    verify(libraryUserService, times(0))
        .save(any());
  }

  @Test
  void updateShouldReturnUpdatedLibraryUser() throws Exception {
    var libraryUserForUpdate = getTestLibraryUserRequestDto();

    when(libraryUserService.update(1L, libraryUserForUpdate))
        .thenReturn(getTestLibraryUser());

    mvc.perform(patch(ENDPOINT + "/1")
            .content(asJsonString(getTestLibraryUserRequestDto()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Name"))
        .andExpect(jsonPath("$.surname").value("Surname"))
        .andExpect(jsonPath("$.bookList").isEmpty());

    verify(libraryUserService, times(1))
        .update(1L, libraryUserForUpdate);
  }

  @Test
  void updateShouldReturnUpdatedEntityWhenFieldsAreNull() throws Exception {
    var libraryUserRequestWithNullFields = new LibraryUserRequest();

    when(libraryUserService.update(1L, libraryUserRequestWithNullFields))
        .thenReturn(getTestLibraryUser());

    mvc.perform(patch(ENDPOINT + "/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(libraryUserRequestWithNullFields)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Name"))
        .andExpect(jsonPath("$.surname").value("Surname"))
        .andExpect(jsonPath("$.bookList").isEmpty());

    verify(libraryUserService, times(1))
        .update(1L, libraryUserRequestWithNullFields);
  }

  @Test
  void updateShouldReturnBadRequestWhenFieldsAreEmpty() throws Exception {
    var notValidLibraryUserDTO = getTestLibraryUserRequestDto();
    notValidLibraryUserDTO.setName("");
    notValidLibraryUserDTO.setSurname("");

    mvc.perform(patch(ENDPOINT + "/1")
            .content(asJsonString(notValidLibraryUserDTO))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name")
            .value("Name is not valid!"))
        .andExpect(jsonPath("$.surname")
            .value("Surname is not valid!"));

    verify(libraryUserService, times(0))
        .update(anyLong(), any());
  }

  @Test
  void updateShouldReturnBadRequestWhenFieldsBreakingMaximumLength() throws Exception {
    var maximumLengthPlus1Words = new StringBuilder("F");

    IntStream.rangeClosed(0, 30)
        .forEach(num -> maximumLengthPlus1Words.append("f"));

    var notValidLibraryUserDTO = LibraryUserRequest.builder()
        .name(maximumLengthPlus1Words.toString())
        .surname(maximumLengthPlus1Words.toString())
        .build();

    mvc.perform(patch(ENDPOINT + "/1")
            .content(asJsonString(notValidLibraryUserDTO))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name")
            .value("Length shouldn't be greater than 30!"))
        .andExpect(jsonPath("$.surname")
            .value("Length shouldn't be greater than 30!"));

    verify(libraryUserService, times(0))
        .update(anyLong(), any());
  }

  @Test
  void deleteShouldReturnNoContent() throws Exception {
    when(libraryUserService.delete(1L))
        .thenReturn(getTestLibraryUser());

    mvc.perform(delete(ENDPOINT + "/1"))
        .andExpect(status().isNoContent());

    verify(libraryUserService, times(1))
        .delete(1L);
  }

  private LibraryUser getTestLibraryUser() {
    return LibraryUser.builder()
        .id(1L)
        .name("Name")
        .surname("Surname")
        .bookList(null)
        .build();
  }

  private LibraryUserRequest getTestLibraryUserRequestDto() {
    return LibraryUserRequest.builder()
        .name("Name")
        .surname("Surname")
        .build();
  }

  private String asJsonString(Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}