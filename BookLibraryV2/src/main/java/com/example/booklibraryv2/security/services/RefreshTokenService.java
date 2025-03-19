package com.example.booklibraryv2.security.services;

import com.example.booklibraryv2.security.entitites.RefreshTokenEntity;
import com.example.booklibraryv2.security.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional
  public void save(RefreshTokenEntity refreshTokenEntity) {
    refreshTokenRepository.save(refreshTokenEntity);
  }
}
