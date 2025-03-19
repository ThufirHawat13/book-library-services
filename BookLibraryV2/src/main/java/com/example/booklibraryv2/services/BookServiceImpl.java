package com.example.booklibraryv2.services;

import com.example.booklibraryv2.dto.bookDTO.BookRequest;
import com.example.booklibraryv2.entities.Book;
import com.example.booklibraryv2.exceptions.ServiceException;
import com.example.booklibraryv2.repositories.BookRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;


  @Override
  public List<Book> getAll() {
    return bookRepository.findAll();
  }

  @Override
  public Book findById(Long id) throws ServiceException {
    return findByIdOrThrow(id);
  }

  @Override
  public List<Book> findByNameContains(String searchQuery) {
    return bookRepository.findByNameContains(searchQuery);
  }

  @Override
  public Book save(Book book) {
    Book savedBook = bookRepository.save(book);
    log.info("saved book: {}", savedBook);

    return savedBook;
  }

  @Override
  @Transactional
  public Book update(Long id, BookRequest updateBookDTO) throws ServiceException {
    Book updatedBook = updateFields(findByIdOrThrow(id), updateBookDTO);
    log.info("updated book: {}", updatedBook);

    return updatedBook;
  }

  private Book updateFields(Book bookForUpdate, BookRequest updateBookDTO) {
    Optional.ofNullable(updateBookDTO.getName())
        .ifPresent(bookForUpdate::setName);
    Optional.ofNullable(updateBookDTO.getAuthor())
        .ifPresent(bookForUpdate::setAuthor);
    Optional.ofNullable(updateBookDTO.getYearOfWriting())
        .ifPresent(bookForUpdate::setYearOfWriting);

    return bookForUpdate;
  }

  @Override
  @Transactional
  public void delete(Long id) throws ServiceException {
    Book bookForDelete = findByIdOrThrow(id);
    bookRepository.deleteById(id);
    log.info("deleted book: {}", bookForDelete);
  }

  private Book findByIdOrThrow(Long id) throws ServiceException {
    return bookRepository.findById(id)
        .orElseThrow(() -> new ServiceException(
            "Book with id=%s isn't exists!".formatted(id)));
  }
}
