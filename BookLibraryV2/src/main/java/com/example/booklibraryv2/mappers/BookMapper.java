package com.example.booklibraryv2.mappers;

import com.example.booklibraryv2.dto.bookDTO.BookRequest;
import com.example.booklibraryv2.dto.bookDTO.BookResponse;
import com.example.booklibraryv2.entities.Book;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface BookMapper {

  BookResponse toResponse(Book book);
  List<BookResponse> toResponses(List<Book> books);
  Book toEntity(BookRequest bookRequest);
}
