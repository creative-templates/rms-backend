package com.restaurant.ms.core.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
  private final HttpStatus status;

  public GeneralException(String message) {
    super(message);
    this.status = HttpStatus.BAD_REQUEST;
  }

  public GeneralException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
