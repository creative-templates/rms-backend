package com.restaurant.ms.core.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant.ms.auth.enums.EAuthProvider;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseModel implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank
  @Size(max = 50)
  private String firstName;

  @NotBlank
  @Size(max = 50)
  private String lastName;

  @NotBlank
  @Email()
  @Column(unique = true, nullable = false)
  private String email;

  @NotBlank
  @Size(min = 4, max = 12)
  @Column(unique = true, nullable = false)
  private String username;

  @JsonIgnore
  private String password;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  @ManyToOne
  @JoinColumn(name = "outlet_id")
  private Outlet outlet;

  private String tel;

  @Enumerated(EnumType.STRING)
  @Column(name = "auth_provider", nullable = false)
  private EAuthProvider authProvider;

  @Column(name = "verified_email", nullable = false)
  private boolean verifiedEmail = false;

  @Column(name = "profile_picture")
  private String profilePicture;

  @Column(nullable = false)
  private boolean enabled = false;

  public String getFullname() {
    return this.firstName + " " + this.lastName;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    HashSet<GrantedAuthority> auths = new HashSet<>();

    auths.add(new SimpleGrantedAuthority("ROLE_" + getRole().getName()));
    getRole()
        .getPermissions()
        .forEach(p -> auths.add(new SimpleGrantedAuthority(p.getName().toString())));

    return auths;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

}
