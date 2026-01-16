package com.restaurant.ms.auth.payloads;

import com.restaurant.ms.core.annotations.PasswordMatches;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@PasswordMatches(passwordField = "password", confirmPasswordField = "confirmPassword")
public class ResetPasswordDto {
  @NotEmpty
  private String username;

  @NotEmpty
  private String otp;

  @NotEmpty
  private String password;

  @NotEmpty
  private String confirmPassword;
}
