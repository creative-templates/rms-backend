package com.restaurant.ms.auth.payloads;

import com.restaurant.ms.auth.enums.EAuthProvider;
import com.restaurant.ms.core.annotations.ValidEmail;
import com.restaurant.ms.core.models.Role;
import com.restaurant.ms.core.models.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountDto {
  @NotBlank
  private String firstname;

  @NotBlank
  private String lastname;

  @ValidEmail
  @NotBlank
  private String email;

  @NotBlank
  private String username;

  @NotNull
  private Role role;

  private String tel;

  private String profilePicture;

  public User toUser() {
    User user = new User();
    user.setAuthProvider(EAuthProvider.LOCAL);
    user.setFirstName(firstname);
    user.setLastName(lastname);
    user.setEmail(email);
    user.setEnabled(false);
    user.setRole(role);
    user.setUsername(username);
    user.setPassword(null);
    user.setVerifiedEmail(false);

    return user;
  }
}
