package com.restaurant.ms.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.ms.core.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  User findByUsername(String username);
}
