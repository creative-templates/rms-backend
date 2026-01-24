package com.restaurant.ms.core.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.ms.core.enums.EPermission;
import com.restaurant.ms.core.models.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
  public boolean existsByName(EPermission name);
  public List<Permission> findAllByNameIn(List<EPermission> permissions);
}
