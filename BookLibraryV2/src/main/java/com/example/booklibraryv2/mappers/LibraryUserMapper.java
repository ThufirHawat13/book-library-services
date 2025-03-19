package com.example.booklibraryv2.mappers;

import com.example.booklibraryv2.dto.libraryUserDTO.LibraryUserRequest;
import com.example.booklibraryv2.dto.libraryUserDTO.LibraryUserResponse;
import com.example.booklibraryv2.entities.LibraryUser;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface LibraryUserMapper {

  LibraryUserResponse toResponse(LibraryUser libraryUser);
  List<LibraryUserResponse> toResponses(List<LibraryUser> libraryUsers);
  LibraryUser toEntity(LibraryUserRequest libraryUserRequest);
}
