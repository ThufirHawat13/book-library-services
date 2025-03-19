package com.example.booklibraryv2.services;

import com.example.booklibraryv2.dto.libraryUserDTO.LibraryUserRequest;
import com.example.booklibraryv2.entities.LibraryUser;
import com.example.booklibraryv2.exceptions.ServiceException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface LibraryUserService {

  List<LibraryUser> getAll();

  LibraryUser findById(Long id);

  List<LibraryUser> findByNameContains(String searchQuery);

  LibraryUser save(LibraryUser newLibraryUser);

  LibraryUser update(Long id, LibraryUserRequest updatedFields) throws ServiceException;

  LibraryUser delete(Long id);
}
