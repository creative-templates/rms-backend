package com.restaurant.ms.core.validators;

import com.restaurant.ms.auth.payloads.RegisterAccountDto;
import com.restaurant.ms.core.annotations.PasswordMatches;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
  @Override
  public void initialize(PasswordMatches constraintAnnotation) {
    //
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    RegisterAccountDto user = (RegisterAccountDto) value;

    return user.getPassword().equals(user.getConfirmPassword());
  }
}
