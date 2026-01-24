package com.restaurant.ms.user.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.ms.core.models.Role;
import com.restaurant.ms.core.payloads.GeneralResponse;
import com.restaurant.ms.core.services.RoleService;
import com.restaurant.ms.user.payloads.CreateRoleDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {
  private final RoleService roleService;

  @GetMapping("all")
  @PreAuthorize("hasAuthority('READ_ROLE')")
  public ResponseEntity<GeneralResponse> getRoles() {
    return ResponseEntity.ok().body(new GeneralResponse(roleService.getRoles()));
  }

  @PostMapping("create")
  @PreAuthorize("hasAuthority('CREATE_ROLE')")
  public ResponseEntity<GeneralResponse> createRole(@Valid @RequestBody CreateRoleDto dto) {
    return ResponseEntity.ok().body(new GeneralResponse(roleService.createRole(dto)));
  }

  @PostMapping("update")
  public ResponseEntity<GeneralResponse> updateRole(@Valid @RequestBody Role updatedRole) {
    return ResponseEntity.ok().body(new GeneralResponse(roleService.updateRole(updatedRole)));
  }
}
