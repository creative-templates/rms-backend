package com.restaurant.ms.core.services;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.restaurant.ms.core.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

  @Value("${rms.app.jwtSecret}")
  private String jwtSecret;

  @Value("${rms.app.accessToken.expTime}")
  private long accessTokenExpTime;

  @Value("${rms.app.refreshCookie.expTime}")
  private long refreshCookieExpTime;

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

  public String getAccessToken(User user) {
    return generateToken(
        Map.of("role", user.getRole()),
        user.getUsername(),
        accessTokenExpTime);
  }

  public String getRefreshCookie(User user) {
    String token = generateToken(
        Map.of("role", user.getRole()),
        user.getUsername(),
        1440 // 1 day
    );

    ResponseCookie cookie = ResponseCookie
        .from("refresh_token", token)
        .httpOnly(true)
        .secure(true)
        .sameSite("Lax")
        .path("/")
        .maxAge(Duration.ofDays(1))
        .build();

    return cookie.toString();
  }
}
