package com.restaurant.ms.core.annotations;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.restaurant.ms.core.validators.PasswordMatchesValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
  String message() default "Passwords do not match";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String passwordField() default "password";

  String confirmPasswordField() default "confirmPassword";
}
