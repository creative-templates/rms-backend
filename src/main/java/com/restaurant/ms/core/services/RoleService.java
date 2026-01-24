package com.restaurant.ms.core.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.restaurant.ms.core.exceptions.GeneralException;
import com.restaurant.ms.core.models.Role;
import com.restaurant.ms.core.repositories.RoleRepository;
import com.restaurant.ms.user.payloads.CreateRoleDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
  private final RoleRepository roleRepository;

  public Role createRole(CreateRoleDto dto) {
    return roleRepository.save(dto.toRole());
  }

  public List<Role> getRoles() {
    return roleRepository.findAll();
  }

  public Role updateRole(Role updatedRole) {
    boolean isRolePresent = roleRepository.existsById(updatedRole.getId());

    if (!isRolePresent) {
      throw new GeneralException("Invalid role");
    }

    return roleRepository.save(updatedRole);
  }

  public void deleteRole(UUID id) {
    boolean isRolePresent = roleRepository.existsById(id);

    if (isRolePresent) {
      throw new GeneralException("Invalid id");
    }

    roleRepository.deleteById(id);
  }
}
