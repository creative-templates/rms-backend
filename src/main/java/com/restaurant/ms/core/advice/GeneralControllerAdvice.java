package com.restaurant.ms.core.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.restaurant.ms.core.exceptions.GeneralException;
import com.restaurant.ms.core.payloads.GeneralResponse;

@ControllerAdvice
public class GeneralControllerAdvice {
  @ExceptionHandler({ GeneralException.class })
  @ResponseBody
  public ResponseEntity<GeneralResponse> handleGeneralException(GeneralException ex) {
    GeneralResponse response = new GeneralResponse(ex.getMessage());

    return new ResponseEntity<>(response, ex.getStatus());
  }
}
