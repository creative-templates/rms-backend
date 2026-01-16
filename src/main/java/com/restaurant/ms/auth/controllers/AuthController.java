package com.restaurant.ms.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.ms.auth.payloads.AuthenticatedUserDto;
import com.restaurant.ms.auth.payloads.LoginAccountDto;
import com.restaurant.ms.auth.payloads.RegisterAccountDto;
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

  @PostMapping("register")
  public ResponseEntity<GeneralResponse> register(
      @Valid @RequestBody RegisterAccountDto dto,
      HttpServletRequest request,
      HttpServletResponse response) {
    AuthenticatedUserDto data = authService.register(dto, request, response);
    return ResponseEntity.ok().body(new GeneralResponse(data));
  }

  @GetMapping("verify-email")
  public ResponseEntity<GeneralResponse> activateAccount(@RequestParam("token") String token) {
    authService.verifyEmail(token);

    return ResponseEntity.ok().body(new GeneralResponse("Your account is activated"));
  }

  @PostMapping("login")
  public ResponseEntity<GeneralResponse> login(@Valid @RequestBody LoginAccountDto dto,
          HttpServletResponse response) {
    AuthenticatedUserDto result = authService.login(dto, response);
    return ResponseEntity.ok(new GeneralResponse(result));

  }

  @GetMapping("forget-password")
  public ResponseEntity<GeneralResponse> forgetPassword(@RequestParam("username") String username) {
    authService.forgetPassword(username);
    return ResponseEntity.ok(new GeneralResponse("OTP sent if account exists"));

  }

  @PostMapping("forget-password/verify-otp")
  public ResponseEntity<GeneralResponse> verifyOtp(@Valid @RequestBody VerifyOtpDto dto) {
    authService.verifyOtp(dto.getUsername(), dto.getOtp());
    return ResponseEntity.ok(new GeneralResponse(true));
  }

  @PostMapping("forget-password/reset-password")
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

}
