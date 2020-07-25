package com.jcampos.campdemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DatesAlreadyBookedException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public DatesAlreadyBookedException(String message){
    super(message);
  }
}
