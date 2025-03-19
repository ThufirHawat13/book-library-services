package com.example.booklibraryv2.dto.bookDTO;

import com.example.booklibraryv2.dto.libraryUserDTO.LibraryUserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

  private Long id;
  private String name;
  private String author;
  private Integer yearOfWriting;
  private LibraryUserResponse holder;
}
