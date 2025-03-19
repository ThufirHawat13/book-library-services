package com.example.booklibraryv2.services;

import com.example.booklibraryv2.dto.bookDTO.BookRequest;
import com.example.booklibraryv2.entities.Book;
import com.example.booklibraryv2.exceptions.ServiceException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface BookService {

  List<Book> getAll();

  Book findById(Long id) throws ServiceException;

  List<Book> findByNameContains(String searchQuery);

  Book save(Book book);

  Book update(Long id, BookRequest updateBookDTO) throws ServiceException;

  void delete(Long id) throws ServiceException;
}
