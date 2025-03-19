package com.example.booklibraryv2.security.models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
  private String username;
  private String password;

}
