package com.example.booklibraryv2.security.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.booklibraryv2.security.models.userPrincipal.UserPrincipal;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtToPrincipalConverter {

  public UserPrincipal convert(DecodedJWT decodedJwt) {
    return UserPrincipal.builder()
        .id(Long.valueOf(decodedJwt.getSubject()))
        .username(decodedJwt.getClaim("u").asString())
        .authorities(extractAuthoritiesFromClaim(decodedJwt))
        .build();
  }

  private List<SimpleGrantedAuthority> extractAuthoritiesFromClaim(DecodedJWT decodedJwt) {
    Claim claim = decodedJwt.getClaim("a");

    return (claim.isNull() || claim.isMissing())
        ? Collections.emptyList()
        : claim.asList(SimpleGrantedAuthority.class);
  }
}
