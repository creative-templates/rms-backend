package com.restaurant.ms.core.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.restaurant.ms.core.enums.EPermission;
import com.restaurant.ms.core.models.Permission;
import com.restaurant.ms.core.repositories.PermissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService {
  private final PermissionRepository permissionRepository;

  public List<Permission> getAllPermissions(List<EPermission> permissions) {
    if (permissions.isEmpty()) {
      return permissionRepository.findAll();
    }
    return permissionRepository.findAllByNameIn(permissions);
  }

}
