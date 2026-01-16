package com.restaurant.ms.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant.ms.core.roles.ERole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotBlank
  @Size(max = 50)
  private String firstName;

  @NotBlank
  @Size(max = 50)
  private String lastName;

  @NotBlank
  @Column(unique = true)
  @Email()
  private String email;

  @NotBlank
  @Column(unique = true)
  @Size(min = 4, max = 12)
  private String username;

  @JsonIgnore
  private String password;

  @Enumerated(EnumType.STRING)
  private ERole role = ERole.CUSTOMER;

  private String tel;

  @Column(name = "enabled")
  private boolean enabled = false;

  public String getFullname() {
    return this.firstName + " " + this.lastName;
  }
}
