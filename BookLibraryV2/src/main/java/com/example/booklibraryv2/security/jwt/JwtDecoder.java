package com.example.booklibraryv2.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.booklibraryv2.security.configs.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder {
  private final JwtProperties jwtProperties;

  public DecodedJWT decodeJwt(String token) {
    return JWT.require(Algorithm.HMAC256(jwtProperties.getAccessTokenSecretKey()))
        .build()
        .verify(token);
  }

  public DecodedJWT decodeRefreshToken(String token) {
    return JWT.require(Algorithm.HMAC256(jwtProperties.getRefreshTokenSecretKey()))
        .build()
        .verify(token);
  }
}
