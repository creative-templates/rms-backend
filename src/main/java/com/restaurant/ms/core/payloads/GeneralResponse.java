package com.restaurant.ms.core.payloads;

import lombok.Getter;

@Getter
public class GeneralResponse {
  private String message;
  private Object data;

  public GeneralResponse(String message) {
    this.message = message;
  }

  public GeneralResponse(Object data) {
    this.data = data;
  }

  public GeneralResponse(String message, Object data) {
    this.message = message;
    this.data = data;
  }
}
