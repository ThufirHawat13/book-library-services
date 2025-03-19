package com.example.booklibraryv2.security.controllers;

import com.example.booklibraryv2.security.models.LoginRequest;
import com.example.booklibraryv2.security.models.LoginResponse;
import com.example.booklibraryv2.security.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthControllerV1 {
  private final AuthService authService;

  @Tag(name = "Auth API")
  @PostMapping("/login")
  public LoginResponse login(@RequestBody @Validated LoginRequest loginRequest) {
    return authService.attemptLogin(loginRequest.getUsername(), loginRequest.getPassword());
  }

  @Tag(name = "Auth API")
  @GetMapping("/refresh-tokens")
  public LoginResponse refreshTokens(HttpServletRequest httpServletRequest) {
    return authService.tryToRefreshTokens(httpServletRequest);
  }
}
