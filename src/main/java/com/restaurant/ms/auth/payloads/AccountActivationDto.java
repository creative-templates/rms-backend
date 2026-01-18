package com.restaurant.ms.auth.payloads;

import com.restaurant.ms.core.annotations.PasswordMatches;

import lombok.Data;

@Data
@PasswordMatches(passwordField = "password", confirmPasswordField = "confirmPassword")
public class AccountActivationDto {
  private String token;
  private String password;
  private String confirmPassword;
}
