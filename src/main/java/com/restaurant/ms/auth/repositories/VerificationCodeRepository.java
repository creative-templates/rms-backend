package com.restaurant.ms.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.ms.auth.enums.EStatus;
import com.restaurant.ms.auth.models.VerificationCode;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {
  VerificationCode findByUsernameAndCodeAndType(String username, String code, EStatus type);
}
