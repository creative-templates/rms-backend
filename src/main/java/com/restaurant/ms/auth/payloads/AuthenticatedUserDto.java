package com.restaurant.ms.auth.payloads;

import java.util.UUID;

import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.roles.ERole;

import lombok.Getter;

@Getter
public class AuthenticatedUserDto {
  private UUID id;
  private String firstname;
  private String lastname;
  private String email;
  private String username;
  private ERole role;
  private String fullName;
  private String accessToken;

  public AuthenticatedUserDto(User user, String accessToken) {
    this.id = user.getId();
    this.firstname = user.getFirstName();
    this.lastname = user.getLastName();
    this.email = user.getEmail();
    this.username = user.getUsername();
    this.role = user.getRole();
    this.fullName = user.getFullname();
    this.accessToken = accessToken;
  }
}
