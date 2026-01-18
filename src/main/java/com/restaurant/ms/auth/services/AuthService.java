package com.restaurant.ms.auth.services;

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
import com.restaurant.ms.auth.payloads.CreateAccountDto;
import com.restaurant.ms.auth.payloads.LoginAccountDto;
import com.restaurant.ms.auth.repositories.VerificationCodeRepository;
import com.restaurant.ms.core.exceptions.GeneralException;
import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.repositories.UserRepository;
import com.restaurant.ms.core.services.EmailService;
import com.restaurant.ms.core.services.TokenService;

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

  public void createUser(CreateAccountDto dto, HttpServletRequest request) {
    User existingUser = userRepository.findByUsername(dto.getUsername()).orElse(null);

    if (existingUser != null) {
      throw new GeneralException("Username already exists");
    }

    User user = dto.toUser();
    userRepository.save(user);

    sendAccountActivationLink(user, request);
  }

  public AuthenticatedUserDto login(LoginAccountDto dto, HttpServletResponse response) {
    User user = userRepository.findByUsername(dto.getUsername())
        .orElseThrow(() -> new GeneralException("Invalid Credentails"));

    if (!user.isEnabled()) {
      throw new GeneralException("Account is disabled. Please activate your account or contact customer care");
    }
    Authentication authentication = authenticationManager
        .authenticate(
            new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

    setRefreshCookie(response, user);
    String accessToken = generateAccessToken(user);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    System.out.println("Roles: " + authentication.getAuthorities());

    return new AuthenticatedUserDto(user, accessToken);
  }

  public void activateAccount(String token, String passsword) {
    if (!tokenService.validateToken(token)) {
      throw new GeneralException("Token expired");
    }

    User user = userRepository.findByUsername(tokenService.extractUsername(token))
        .orElseThrow(() -> new GeneralException("Invalid token"));

    user.setVerifiedEmail(true);
    user.setEnabled(true);
    user.setPassword(passwordEncoder.encode(passsword));
    userRepository.save(user);
  }

  public void forgetPassword(String username) {
    User user = userRepository.findByUsername(username).orElse(null);

    if (user == null)
      return;

    verificationCodeRepository.deleteAllByUserAndType(user, EStatus.FORGOT_PASSWORD);

    VerificationCode verificationCode = VerificationCode.create(user, EStatus.FORGOT_PASSWORD);
    verificationCodeRepository.save(verificationCode);

    emailService.sendText(
        user.getEmail(),
        "Forget password",
        "Your OTP is: " + verificationCode.getCode());
  }

  public void verifyOtp(String username, String code) {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new GeneralException("Invalid otp"));

    VerificationCode verificationCode = verificationCodeRepository.findByUserAndCodeAndType(user, code,
        EStatus.FORGOT_PASSWORD);

    if (verificationCode == null || verificationCode.isExpired())
      throw new GeneralException("Invalid OTP");

    verificationCode.setType(EStatus.RESET_PASSWORD);
    verificationCodeRepository.save(verificationCode);
  }

  public void resetPassword(String username, String password, String code) {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new GeneralException("Invalid token"));

    VerificationCode verificationCode = verificationCodeRepository.findByUserAndCodeAndType(user, code,
        EStatus.RESET_PASSWORD);

    if (verificationCode == null) {
      throw new GeneralException("Invalid token");
    }

    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);

    verificationCodeRepository.delete(verificationCode);
  }

  public AuthenticatedUserDto refreshToken(String token) {
    if (token == null || !tokenService.validateToken(token)) {
      throw new GeneralException("Unautorized", HttpStatus.UNAUTHORIZED);
    }

    String username = tokenService.extractUsername(token);
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new GeneralException("Unauthorized", HttpStatus.UNAUTHORIZED));

    return new AuthenticatedUserDto(user, generateAccessToken(user));
  }

  public void sendAccountActivationLink(User user, HttpServletRequest request) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("email", user.getEmail());
    claims.put("type", EStatus.ACCOUNT_ACTIVATION);

    String token = tokenService.generateToken(claims, user.getUsername(), 24);

    String url = request.getScheme() + "://" +
        request.getServerName() + ":" +
        request.getServerPort() +
        "/api/auth/activate-account?token=" + token;

    emailService.sendText(
        user.getEmail(),
        "Activate your account",
        "Activate your account" + "\n\n" + url);
  }

  private void setRefreshCookie(HttpServletResponse response, User user) {
    response.addHeader("Set-Cookie", tokenService.getRefreshCookie(user));
  }

  public String generateAccessToken(User user) {
    return tokenService.getAccessToken(user);
  }

  public void logout(HttpServletResponse response) {
    ResponseCookie cookie = ResponseCookie
        .from("refresh_token", "")
        .path("/")
        .maxAge(0)
        .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }
}
