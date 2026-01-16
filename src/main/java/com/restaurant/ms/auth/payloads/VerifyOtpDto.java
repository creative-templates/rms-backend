package com.restaurant.ms.auth.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyOtpDto {
  @NotEmpty
  private String username;

  @Size(min = 6, max = 6)
  private String otp;
}
