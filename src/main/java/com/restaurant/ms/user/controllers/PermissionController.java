package com.restaurant.ms.user.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.ms.core.payloads.GeneralResponse;
import com.restaurant.ms.core.services.PermissionService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {
  private final PermissionService permissionService;

  @GetMapping("all")
  public ResponseEntity<GeneralResponse> getAll() {
      return ResponseEntity.ok().body(new GeneralResponse(permissionService.getAllPermissions(null)));
  }
}
