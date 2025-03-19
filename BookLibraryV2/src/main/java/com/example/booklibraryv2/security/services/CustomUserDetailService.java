package com.example.booklibraryv2.security.services;

import com.example.booklibraryv2.security.entitites.UserEntity;
import com.example.booklibraryv2.security.models.userPrincipal.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userService.findUserByUsername(username);

    return UserPrincipal.builder()
        .id(user.getId())
        .username(user.getUsername())
        .password(user.getPassword())
        .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
        .build();
  }
}
