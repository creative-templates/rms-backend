package com.restaurant.ms.core.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.restaurant.ms.core.annotations.ValidEmail;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
  private static final String EMAIL_PATTERN =
    "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

  @Override
  public void initialize(ValidEmail constraintAnnotation) {
    //
  }

  private boolean validateEmail(String email) {
    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    Matcher matcher = pattern.matcher(email);

    return matcher.matches();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return (validateEmail(value));
  }
}
