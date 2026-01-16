package com.restaurant.ms.auth.services;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restaurant.ms.auth.enums.EStatus;
import com.restaurant.ms.auth.models.VerificationCode;
import com.restaurant.ms.auth.payloads.AuthenticatedUserDto;
import com.restaurant.ms.auth.payloads.LoginAccountDto;
import com.restaurant.ms.auth.payloads.RegisterAccountDto;
import com.restaurant.ms.auth.repositories.VerificationCodeRepository;
import com.restaurant.ms.core.exceptions.GeneralException;
import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.repositories.UserRepository;
import com.restaurant.ms.core.services.EmailService;
import com.restaurant.ms.core.services.TokenService;
import com.restaurant.ms.core.utils.OtpCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;
  private final VerificationCodeRepository verificationCodeRepository;

  public AuthenticatedUserDto register(RegisterAccountDto dto, HttpServletRequest request,
      HttpServletResponse response) {
    if (userRepository.findByUsername(dto.getUsername()) != null) {
      throw new GeneralException("Username already exists");
    }

    User user = dto.toUser();
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);

    sendVerificationEmail(user, request);

    setRefreshCookie(response, user);
    String accessToken = generateAccessToken(user);

    Authentication authentication = authenticationManager
        .authenticate(
            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    return new AuthenticatedUserDto(user, accessToken);
  }

  public AuthenticatedUserDto login(LoginAccountDto dto, HttpServletResponse response) {
    authenticationManager
        .authenticate(
            new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
    User user = userRepository.findByUsername(dto.getUsername());

    setRefreshCookie(response, user);
    String accessToken = generateAccessToken(user);

    Authentication authentication = authenticationManager
        .authenticate(
            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    return new AuthenticatedUserDto(user, accessToken);
  }

  public void verifyEmail(String token) {
    if (!tokenService.validateToken(token)) {
      throw new GeneralException("Token expired");
    }

    User user = userRepository.findByUsername(tokenService.extractUsername(token));
    user.setVerifiedEmail(true);
    userRepository.save(user);
  }

  public void forgetPassword(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null)
      return;

    String otp = OtpCode.generateOtp();

    VerificationCode verificationCode = VerificationCode.create(username, EStatus.FORGOT_PASSWORD);
    verificationCodeRepository.save(verificationCode);

    emailService.sendText(
        user.getEmail(),
        "Forget password",
        "Your OTP is: " + otp);
  }

  public void verifyOtp(String username, String code) {
    VerificationCode verificationCode = verificationCodeRepository.findByUsernameAndCodeAndType(username, code,
        EStatus.FORGOT_PASSWORD);

    if (verificationCode == null)
      throw new GeneralException("Invalid OTP");

    verificationCode.setType(EStatus.RESET_PASSWORD);
    verificationCodeRepository.save(verificationCode);
  }

  public void resetPassword(String username, String password, String code) {
    VerificationCode verificationCode = verificationCodeRepository.findByUsernameAndCodeAndType(
        username,
        code,
        EStatus.RESET_PASSWORD);

    if (verificationCode == null) {
      throw new GeneralException("Invalid token");
    }

    User user = userRepository.findByUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);

    verificationCodeRepository.delete(verificationCode);
  }

  public AuthenticatedUserDto refreshToken(String token) {
    if (token == null || !tokenService.validateToken(token)) {
      throw new GeneralException("Unautorized", HttpStatus.UNAUTHORIZED);
    }

    String username = tokenService.extractUsername(token);
    User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new GeneralException("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    return new AuthenticatedUserDto(user, generateAccessToken(user));
  }

  public void sendVerificationEmail(User user, HttpServletRequest request) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("email", user.getEmail());
    claims.put("type", EStatus.ACCOUNT_REGISTRATION_VERIFICATION);

    String token = tokenService.generateToken(claims, user.getUsername(), 24);
    String url = request.getScheme() + "://" +
        request.getServerName() + ":" +
        request.getServerPort() +
        "/api/auth/verify-email?token=" + token;
    emailService.sendText(
        user.getEmail(),
        "Registration Confirmation",
        "URL" + "\n\n" + url);
  }

  private void setRefreshCookie(HttpServletResponse response, User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", user.getUsername());
    claims.put("role", user.getRole());

    String token = tokenService.generateToken(
        Map.of("role", user.getRole()),
        user.getUsername(),
        1440 // 1 day
    );

    ResponseCookie cookie = ResponseCookie
        .from("refresh_token", token)
        .httpOnly(true)
        .secure(true)
        .sameSite("Lax")
        .path("/")
        .maxAge(Duration.ofDays(1))
        .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }

  public String generateAccessToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", user.getUsername());
    claims.put("role", user.getRole());

    return tokenService.generateToken(claims, user.getUsername(), 15);
  }
}
