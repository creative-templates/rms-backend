package com.restaurant.ms.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.ms.auth.payloads.LoginAccountDto;
import com.restaurant.ms.auth.payloads.RegisterAccountDto;
import com.restaurant.ms.auth.payloads.ResetPasswordDto;
import com.restaurant.ms.auth.payloads.VerifyOtpDto;
import com.restaurant.ms.auth.services.AuthService;
import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.payloads.GeneralResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("register")
  public ResponseEntity<GeneralResponse> register(@Valid @RequestBody RegisterAccountDto newAccount,
      HttpServletRequest request) {
    User response = authService.register(newAccount.toUser());
    String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    authService.sendRegistrationConfirmation(response, appUrl);

    return ResponseEntity.ok().body(new GeneralResponse(response));
  }

  @GetMapping("verify-email")
  public ResponseEntity<GeneralResponse> activateAccount(@RequestParam("token") String token) {
    authService.activateAccount(token);

    return ResponseEntity.ok().body(new GeneralResponse("Your account is activated"));
  }

  @PostMapping("login")
  public ResponseEntity<GeneralResponse> login(@Valid @RequestBody LoginAccountDto login) {
    User response = authService.login(login.getUsername(), login.getPassword());
    return ResponseEntity.ok().body(new GeneralResponse(response));
  }

  @GetMapping("forgot-password")
  public ResponseEntity<GeneralResponse> forgetPassword(@RequestParam("username") String username) {
    String response = authService.forgetPassword(username);

    return ResponseEntity.ok().body(new GeneralResponse(response));
  }

  @PostMapping("forgot-password/verify-otp")
  public ResponseEntity<GeneralResponse> verifyOtp(@Valid @RequestBody VerifyOtpDto otpRequest) {
    boolean isSuccess = authService.verifyOtp(otpRequest.getUsername(), otpRequest.getOtp());

    if (!isSuccess) {
      return ResponseEntity.badRequest().body(new GeneralResponse("Invalid otp"));
    }

    return ResponseEntity.ok().body(new GeneralResponse(true));
  }

  @PostMapping("forgot-password/reset-password")
  public ResponseEntity<GeneralResponse> resetPassword(@Valid @RequestBody ResetPasswordDto request) {
    authService.resetPassword(request.getUsername(), request.getPassword(), request.getOtp());

    return ResponseEntity.ok().body(new GeneralResponse("Password has been changed"));
  }

}
