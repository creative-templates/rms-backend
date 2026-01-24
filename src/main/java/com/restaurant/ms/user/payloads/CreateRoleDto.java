package com.restaurant.ms.user.payloads;
import java.util.Set;

import com.restaurant.ms.core.models.Permission;
import com.restaurant.ms.core.models.Role;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateRoleDto {
  @NotEmpty
  private String name;
  private Set<Permission> permissions;

  public Role toRole() {
    Role role = new Role();

    role.setName(name);
    role.setPermissions(permissions);

    return role;
  }
}
