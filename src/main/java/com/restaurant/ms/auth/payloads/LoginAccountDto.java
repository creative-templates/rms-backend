package com.restaurant.ms.auth.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginAccountDto {
  @NotBlank
  private String username;

  @NotBlank
  private String password;
}
