package com.example.booklibraryv2.repositories;

import com.example.booklibraryv2.entities.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  List<Book> findByNameContains(String searchQuery);
}
