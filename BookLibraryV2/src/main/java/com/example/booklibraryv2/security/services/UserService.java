package com.example.booklibraryv2.security.services;

import com.example.booklibraryv2.security.entitites.UserEntity;
import com.example.booklibraryv2.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  public UserEntity findUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByUsername(username).orElseThrow(
        () -> new UsernameNotFoundException("User %s isn't found!".formatted(username)));

    return user;
  }

  public UserEntity findById(Long id) {
    return userRepository.findById(id).orElseThrow(
            () -> new UsernameNotFoundException("User with id = %d isn't found!".formatted(id)));
  }
}
