package com.restaurant.ms.auth.payloads;

import com.restaurant.ms.core.annotations.PasswordMatches;
import com.restaurant.ms.core.annotations.ValidEmail;
import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.roles.ERole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@PasswordMatches
public class RegisterAccountDto {
  @NotBlank
  private String firstname;

  @NotBlank
  private String lastname;

  @ValidEmail
  @NotBlank
  private String email;

  @NotBlank
  private String username;

  @NotBlank
  private String password;

  @NotBlank
  private String confirmPassword;

  @NotNull
  private ERole role = ERole.CUSTOMER;


  private String tel;

  public User toUser() {
    return new User(
        null,
        firstname,
        lastname,
        email,
        username,
        password,
        role,
        tel,
        false);
  }
}
