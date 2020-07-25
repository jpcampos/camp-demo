package com.jcampos.campdemo.model.entity;

import com.jcampos.campdemo.model.enums.BookingStatus;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "booking")
public class Booking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @Column (name = "ARRIVAL_DT", nullable = false, unique = true)
  private LocalDate arrivalDate;

  @Column (name = "DEPARTURE_DT", nullable = false)
  private LocalDate departureDate;

  @Column(name="FIRST_NAME", length=50, nullable=false)
  private String firstName;

  @Column(name="LAST_NAME", length=50, nullable=false)
  private String lastName;

  @Column(name="EMAIL", length=100, nullable=false)
  private String email;

  @Column(name="STATUS",length=10)
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Booking )) return false;
    return id != null && id.equals(((Booking) o).getId());
  }

  @Override
  public int hashCode() {
    return 31;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
