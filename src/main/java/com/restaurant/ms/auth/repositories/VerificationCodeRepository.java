package com.restaurant.ms.auth.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.ms.auth.enums.EStatus;
import com.restaurant.ms.auth.models.VerificationCode;
import com.restaurant.ms.core.models.User;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {
  VerificationCode findByUserAndCodeAndType(User user, String code, EStatus type);

  void deleteAllByUserAndType(User user, EStatus type);
}
