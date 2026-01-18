package com.restaurant.ms.auth.models;

import java.time.Instant;
import java.util.UUID;

import com.restaurant.ms.auth.enums.EStatus;
import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.utils.OtpCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, length = 6)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EStatus type;

  @Column(nullable = false)
  private Instant expiresAt;

  public static VerificationCode create(User username, EStatus type) {
    VerificationCode v = new VerificationCode();
    v.setUser(username);
    v.setCode(OtpCode.generateOtp());
    v.setExpiresAt(Instant.now().plusSeconds(300)); // 5 min
    v.setType(type);

    return v;
  }

  public boolean isExpired() {
    return Instant.now().isAfter(expiresAt);
  }
}
