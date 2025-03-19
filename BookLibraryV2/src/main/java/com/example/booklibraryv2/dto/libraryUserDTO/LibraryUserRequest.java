package com.example.booklibraryv2.dto.libraryUserDTO;

import com.example.booklibraryv2.dto.validationGroups.CreateGroup;
import com.example.booklibraryv2.dto.validationGroups.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryUserRequest {
  //TODO add groups

  @NotNull(message = "Name shouldn't be null!",
      groups = CreateGroup.class)
  @Pattern(regexp = "^[A-Z][a-z]+$",
      message = "Name is not valid!",
      groups = {CreateGroup.class, UpdateGroup.class})
  @Size(max = 30, message = "Length shouldn't be greater than 30!",
      groups = {CreateGroup.class, UpdateGroup.class})
  private String name;
  @NotNull(message = "Surname shouldn't be empty",
      groups = CreateGroup.class)
  @Pattern(regexp = "^[A-Z][a-z]+$",
      message = "Surname is not valid!",
      groups = {CreateGroup.class, UpdateGroup.class})
  @Size(max = 30, message = "Length shouldn't be greater than 30!",
      groups = {CreateGroup.class, UpdateGroup.class})
  private String surname;
}
