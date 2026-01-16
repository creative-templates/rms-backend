package com.restaurant.ms.core.validators;

import java.lang.reflect.Method;

import com.restaurant.ms.core.annotations.PasswordMatches;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

  private String passwordFieldName;
  private String confirmPasswordFieldName;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
      this.passwordFieldName = constraintAnnotation.passwordField();
      this.confirmPasswordFieldName = constraintAnnotation.confirmPasswordField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
      try {
        Class<?> clazz = value.getClass();
        Method passwordGetter = clazz.getMethod("get" + capitalize(passwordFieldName));
        Method confirmPasswordGetter = clazz.getMethod("get" + capitalize(confirmPasswordFieldName));

        Object password = passwordGetter.invoke(value);
        Object confirmPassword = confirmPasswordGetter.invoke(value);

        if (password == null || confirmPassword == null) {
          return false;
        }

        return password.equals(confirmPassword);

      } catch (Exception e) {
        // Log exception if needed
        return false;
      }
    }

    private String capitalize(String str) {
      if (str == null || str.isEmpty())
        return str;
      return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
