package com.restaurant.ms.auth.models;

import java.time.Instant;

import com.restaurant.ms.auth.enums.EStatus;
import com.restaurant.ms.core.utils.OtpCode;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "otp")
public class VerificationCode {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String username;
  private String code;

  @Enumerated(EnumType.STRING)
  private EStatus type;

  private Instant expiresAt;

  public static VerificationCode create(String username, EStatus type) {
    VerificationCode v = new VerificationCode();
    v.setUsername(username);
    v.setCode(OtpCode.generateOtp());
    v.setExpiresAt(Instant.now().plusSeconds(300)); // 5 min
    v.setType(type);

    return v;
  }
}
