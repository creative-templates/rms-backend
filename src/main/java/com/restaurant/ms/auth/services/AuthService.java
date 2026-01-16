package com.restaurant.ms.auth.services;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restaurant.ms.auth.enums.EAuthToken;
import com.restaurant.ms.core.exceptions.GeneralException;
import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.repositories.UserRepository;
import com.restaurant.ms.core.services.EmailService;
import com.restaurant.ms.core.services.TokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final TokenService tokenService;
  private final MessageSource messages;
  private final AuthenticationManager authenticationManager;

  public User register(User user) {
    User existingUser = userRepository.findByUsername(user.getUsername());

    if (existingUser != null) {
      throw new GeneralException("User already present");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
  }

  public void sendRegistrationConfirmation(User user, String appUrl, Locale locale) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("email", user.getEmail());
    claims.put("type", EAuthToken.ACCOUNT_REGISTRATION_VERIFICATION);

    String token = tokenService.generateToken(claims, user.getUsername(), 24);
    String confirmationUrl = appUrl + "/api/auth/registration-confirm?token=" +
        token;
    emailService.sendText(
        user.getEmail(),
        "Registration Confirmation",
        "URL" + "\n\n" + confirmationUrl);
  }

  public User login(String username, String password) {
    User existingUser = userRepository.findByUsername(username);

    if (existingUser == null) {
      throw new GeneralException("Invalid credentials");
    }

    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    return existingUser;
  }

  public void activateAccount(String token) {
    boolean isValid = tokenService.validateToken(token);

    if (!isValid) {
      throw new GeneralException("Token is expired");
    }

    String username = tokenService.extractUsername(token);
    User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new GeneralException("Something went wrong", HttpStatus.BAD_REQUEST);
    }

    user.setEnabled(true);
    userRepository.save(user);
  }
}
