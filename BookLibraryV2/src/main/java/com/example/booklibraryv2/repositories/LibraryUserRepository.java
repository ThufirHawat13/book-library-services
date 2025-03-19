package com.example.booklibraryv2.repositories;

import com.example.booklibraryv2.entities.LibraryUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryUserRepository extends JpaRepository<LibraryUser, Long> {

  List<LibraryUser> findByNameContains(String searchQuery);
}
