package com.restaurant.ms.core.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.ms.core.models.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {}
