package com.example.booklibraryv2.security.jwt;

import com.example.booklibraryv2.security.models.userPrincipal.UserPrincipalAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtDecoder jwtDecoder;
  private final JwtToPrincipalConverter jwtToPrincipalConverter;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    extractTokenFromRequest(request)
        .map(token -> request.getRequestURI().equals("/auth/refresh-tokens")
            ? jwtDecoder.decodeRefreshToken(token)
            : jwtDecoder.decodeJwt(token))
        .map(jwtToPrincipalConverter::convert)
        .map(UserPrincipalAuthenticationToken::new)
        .ifPresent(authentication -> SecurityContextHolder
            .getContext().setAuthentication(authentication));

    filterChain.doFilter(request, response);
  }

  private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader("Authorization");

    return (StringUtils.hasText(token) && token.startsWith("Bearer "))
        ? Optional.of(token.substring(7))
        : Optional.empty();
  }
}
