package com.example.booklibraryv2.controllers;

import com.example.booklibraryv2.dto.bookDTO.BookRequest;
import com.example.booklibraryv2.dto.bookDTO.BookResponse;
import com.example.booklibraryv2.dto.validationGroups.CreateGroup;
import com.example.booklibraryv2.dto.validationGroups.UpdateGroup;
import com.example.booklibraryv2.exceptions.ServiceException;
import com.example.booklibraryv2.mappers.BookMapper;
import com.example.booklibraryv2.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookControllerV1 {

  private final BookService bookService;
  private final BookMapper bookMapper;


  @Tag(name = "Book API")
  @Operation(summary = "get all books",
      description = "returns list of all books")
  @GetMapping
  public List<BookResponse> getAll() {
    return bookMapper.toResponses(
        bookService.getAll());
  }

  @Tag(name = "Book API")
  @Operation(summary = "get book by id",
      description = "returns book with specified id")
  @GetMapping("/{id}")
  public BookResponse getById(
      @PathVariable("id") Long id) throws ServiceException {
    return bookMapper.toResponse(
        bookService.findById(id));
  }

  @Tag(name = "Book API")
  @Operation(summary = "find books by containing of specified line",
      description = "returns list of books that contains specified line in name")
  @GetMapping("/find/{searchQuery}")
  public List<BookResponse> findByNameContains(
      @PathVariable("searchQuery") String searchQuery) {
    return bookMapper.toResponses(
        bookService.findByNameContains(searchQuery));
  }

  @Tag(name = "Book API")
  @Operation(summary = "create new book",
      description = "returns created book")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookResponse create(
      @RequestBody @Validated(CreateGroup.class) BookRequest newBook) {
    return bookMapper.toResponse(
        bookService.save(bookMapper.toEntity(newBook)));
  }

  @Tag(name = "Book API")
  @Operation(summary = "update book fields",
      description = "returns updated book")
  @PatchMapping("/{id}")
  public BookResponse update(
      @PathVariable("id") Long id,
      @RequestBody @Validated(UpdateGroup.class) BookRequest updatedFields)
      throws ServiceException {
    return bookMapper.toResponse(
        bookService.update(id, updatedFields));
  }

  @Tag(name = "Book API")
  @Operation(summary = "delete book by id",
      description = "returns empty body")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @PathVariable("id") Long id) throws ServiceException {
    bookService.delete(id);
  }
}
