package com.restaurant.ms.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.ms.auth.payloads.AccountActivationDto;
import com.restaurant.ms.auth.payloads.AuthenticatedUserDto;
import com.restaurant.ms.auth.payloads.CreateAccountDto;
import com.restaurant.ms.auth.payloads.LoginAccountDto;
import com.restaurant.ms.auth.payloads.ResetPasswordDto;
import com.restaurant.ms.auth.payloads.VerifyOtpDto;
import com.restaurant.ms.auth.services.AuthService;
import com.restaurant.ms.core.payloads.GeneralResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("admin/create-user")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  public ResponseEntity<GeneralResponse> createUser(@Valid @RequestBody CreateAccountDto dto,
          HttpServletRequest request) {
    authService.createUser(dto, request);
    return ResponseEntity.ok().body(new GeneralResponse("Account is created"));
  }

  @PostMapping("activate-account")
  public ResponseEntity<GeneralResponse> activateAccount(@Valid @RequestBody AccountActivationDto dto) {
    authService.activateAccount(dto.getToken(), dto.getPassword());

    return ResponseEntity.ok().body(new GeneralResponse("Your account is activated. Please login"));
  }

  @PostMapping("login")
  public ResponseEntity<GeneralResponse> login(@Valid @RequestBody LoginAccountDto dto,
          HttpServletResponse response) {
    AuthenticatedUserDto result = authService.login(dto, response);
    return ResponseEntity.ok(new GeneralResponse(result));

  }

  @GetMapping("forgot-password")
  public ResponseEntity<GeneralResponse> forgetPassword(@RequestParam("username") String username) {
    authService.forgetPassword(username);
    return ResponseEntity.ok(new GeneralResponse("OTP sent if account exists"));

  }

  @PostMapping("forgot-password/verify-otp")
  public ResponseEntity<GeneralResponse> verifyOtp(@Valid @RequestBody VerifyOtpDto dto) {
    authService.verifyOtp(dto.getUsername(), dto.getOtp());
    return ResponseEntity.ok(new GeneralResponse(true));
  }

  @PostMapping("forgot-password/reset-password")
  public ResponseEntity<GeneralResponse> resetPassword(@Valid @RequestBody ResetPasswordDto dto) {
    authService.resetPassword(dto.getUsername(), dto.getPassword(), dto.getOtp());
    return ResponseEntity.ok(new GeneralResponse("Password changed successfully"));
  }

  @GetMapping("refresh-token")
  public ResponseEntity<GeneralResponse> refreshToken(
      @CookieValue(name = "refresh_token", required = false) String token,
      HttpServletResponse response) {

    AuthenticatedUserDto result = authService.refreshToken(token);
    return ResponseEntity.ok(new GeneralResponse(result));
  }

  @GetMapping("logout")
  public ResponseEntity<GeneralResponse> logout(HttpServletResponse response) {
    authService.logout(response);
    return ResponseEntity.ok().body(new GeneralResponse("Logout successful"));
  }

}
