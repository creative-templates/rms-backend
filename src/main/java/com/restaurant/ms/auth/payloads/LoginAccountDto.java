package com.restaurant.ms.auth.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginAccountDto {
  @NotBlank
  private String username;

  @NotBlank
  private String password;
}
