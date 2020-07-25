package com.jcampos.campdemo.model.enums;

public enum BookingStatus {
  ACTIVE("A"),
  CANCELLED("C"),
  GUEST_EXISTS(  "G"),
  DATES_UNAVAILABLE("U");

  private String code;

  BookingStatus(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
