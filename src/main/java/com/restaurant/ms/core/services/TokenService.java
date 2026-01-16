package com.restaurant.ms.core.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

  @Value("${rms.app.jwtSecret}")
  private String jwtSecret;

  public String generateToken(Map<String, Object> claims, String subject, long minutes) {
    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(new Date())
        .expiration(Date.from(Instant.now().plus(minutes, ChronoUnit.MINUTES)))
        .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
        .compact();
    }

    public Claims extractAllClaims(String token) {
      return Jwts.parser()
          .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
          .build()
          .parseSignedClaims(token)
          .getPayload();
    }

      public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
      }

    public boolean validateToken(String token) {
      try {
        extractAllClaims(token);
        return true;
      } catch (Exception e) {
        return false;
      }
    }
}
