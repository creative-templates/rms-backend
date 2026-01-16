package com.restaurant.ms.core.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.restaurant.ms.core.validators.EmailValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {

  String message() default "Invalid Email";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
