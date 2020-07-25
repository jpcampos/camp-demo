package com.jcampos.campdemo.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.jcampos.campdemo.validation.AreGetOpenDatesCorrect;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;

@AreGetOpenDatesCorrect
public class SearchDates {
  @NotNull
  @JsonSerialize(using = ToStringSerializer.class)
  public LocalDate arrivalDate;
  @JsonSerialize(using = ToStringSerializer.class)
  public LocalDate departureDate;

  public SearchDates(){}
  public SearchDates(LocalDate arrivalDate, LocalDate departureDate) {
    this.arrivalDate = arrivalDate;
    this.departureDate = departureDate;
  }

  public LocalDate getArrivalDate() {
    return arrivalDate;
  }

  public void setArrivalDate(LocalDate arrivalDate) {
    this.arrivalDate = arrivalDate;
  }

  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDate departureDate) {
    this.departureDate = departureDate;
  }
}
