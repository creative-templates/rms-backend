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
import com.restaurant.ms.auth.services.AuthService;
import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.payloads.GeneralResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("register")
  public ResponseEntity<GeneralResponse> register(@Valid @RequestBody RegisterAccountDto newAccount) {
    User response = authService.register(newAccount.toUser());

    return ResponseEntity.ok().body(new GeneralResponse(response));
  }

  @GetMapping("activate-account")
  public ResponseEntity<GeneralResponse> activateAccount(@RequestParam("token") String token) {
    String reponse = authService.activateAccount(token);

    return ResponseEntity.ok().body(new GeneralResponse(reponse));
  }


  @PostMapping("login")
  public ResponseEntity<GeneralResponse> login(@RequestBody LoginAccountDto login) {
    User response = authService.login(login.getUsername(), login.getPassword());
    return ResponseEntity.ok().body(new GeneralResponse(response));
  }

}
