package com.example.booklibraryv2.controllers;

import com.example.booklibraryv2.exceptions.ServiceException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  public Map<String, String> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception) {
    Map<String, String> result = new HashMap<>();

    exception.getBindingResult().getAllErrors().forEach(
        error -> {
          String fieldError = ((FieldError) error).getField();
          String errorMessage = error.getDefaultMessage();
          result.put(fieldError, errorMessage);
        }
    );

    return result;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = {ServiceException.class})
  public Map<String, String> handleServiceException(
      ServiceException exception) {
    Map<String, String> result = new HashMap<>();

    result.put("message", exception.getMessage());

    return result;
  }
}
