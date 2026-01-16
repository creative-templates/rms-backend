package com.restaurant.ms.auth.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restaurant.ms.core.exceptions.GeneralException;
import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User register(User user) {
    User existingUser = userRepository.findByUsername(user.getUsername());

    if (existingUser != null) {
      throw new GeneralException("User already present");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
  }

  public User login(String username, String password) {
    User existingUser = userRepository.findByUsername(username);

    if (existingUser == null) {
      throw new GeneralException("Invalid credentials");
    }

    return existingUser;
  }

  public String activateAccount(String token) {
    return token;
  }
}
