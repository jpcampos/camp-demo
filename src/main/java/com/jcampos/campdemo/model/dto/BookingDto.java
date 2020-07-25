package com.jcampos.campdemo.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.jcampos.campdemo.model.enums.BookingStatus;
import com.jcampos.campdemo.validation.AreBookingsParamsCorrect;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AreBookingsParamsCorrect
public class BookingDto {

  public static final String YYYY_MM_DD = "YYYY-MM-dd";
  private Long id;
  @NotNull
  @JsonSerialize(using = ToStringSerializer.class)
  private LocalDate arrivalDate;
  @NotNull
  @JsonSerialize(using = ToStringSerializer.class)
  private LocalDate departureDate;
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @Email
  private String email;
  private BookingStatus bookingStatus;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public BookingStatus getBookingStatus() {
    return bookingStatus;
  }

  public void setBookingStatus(BookingStatus bookingStatus) {
    this.bookingStatus = bookingStatus;
  }
}
