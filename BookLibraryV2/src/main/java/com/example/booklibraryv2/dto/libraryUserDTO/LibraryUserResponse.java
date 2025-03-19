package com.example.booklibraryv2.dto.libraryUserDTO;

import com.example.booklibraryv2.dto.bookDTO.BookResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryUserResponse {

  private Long id;
  private String name;
  private String surname;
  private List<BookResponse> bookList;
}
