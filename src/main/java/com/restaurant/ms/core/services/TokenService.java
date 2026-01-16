package com.restaurant.ms.core.services;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

  private final SecretKey signingKey;

  public TokenService(@Value("${rms.app.jwtSecret}") String jwtSecret) {
    this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public String generateToken(Map<String, Object> claims, String username, Integer expirationTime) {
      return Jwts.builder()
          .claims(claims)
          .subject(username)
          .issuedAt(new Date(System.currentTimeMillis()))
          .expiration(new Date(System.currentTimeMillis() + expirationTime * 3600000))
          .signWith(signingKey)
          .compact();
    }

    public Claims extractAllClaims(String token) {
      return Jwts.parser()
          .verifyWith(signingKey)
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
