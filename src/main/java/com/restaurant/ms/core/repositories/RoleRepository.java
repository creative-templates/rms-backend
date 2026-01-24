package com.restaurant.ms.core.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.ms.core.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {}
