package com.restaurant.ms.auth.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restaurant.ms.auth.enums.EStatus;
import com.restaurant.ms.auth.models.Otp;
import com.restaurant.ms.auth.repositories.OtpRepository;
import com.restaurant.ms.core.exceptions.GeneralException;
import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.repositories.UserRepository;
import com.restaurant.ms.core.services.EmailService;
import com.restaurant.ms.core.services.TokenService;
import com.restaurant.ms.core.utils.OtpCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;
  private final OtpRepository otpRepository;

  public User register(User user) {
    User existingUser = userRepository.findByUsername(user.getUsername());

    if (existingUser != null) {
      throw new GeneralException("User already present");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
  }

  public void sendRegistrationConfirmation(User user, String appUrl) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("email", user.getEmail());
    claims.put("type", EStatus.ACCOUNT_REGISTRATION_VERIFICATION);

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

    user.setVerifiedEmail(true);
    userRepository.save(user);
  }

  public String forgetPassword(String username) {
    String response = "If the user exists, we have send you otp in mail";

    User user = userRepository.findByUsername(username);

    if (user == null) {
      return response;
    }

    String otp = OtpCode.generateOtp();

    emailService.sendText(
        user.getEmail(),
        "Forget password",
        "URL" + "\n\n" + otp);

    return response;
  }

  public boolean verifyOtp(String username, String code) {
    Otp otp = otpRepository.findByUsernameAndCode(username, code);

    return otp == null;
  }

  public void resetPassword(String username, String password, String code) {
    Otp opt = otpRepository.findByUsernameAndCode(username, code);

    if (opt == null) {
      throw new GeneralException("Invalid token");
    }

    User user = userRepository.findByUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    otpRepository.delete(opt);
  }
}
