package com.restaurant.ms.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.ms.auth.models.Otp;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {
  Otp findByUsernameAndCode(String username, String code);
}
