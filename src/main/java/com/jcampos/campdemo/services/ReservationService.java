package com.jcampos.campdemo.services;

import com.jcampos.campdemo.exception.UserAlreadyHasBookingException;
import com.jcampos.campdemo.model.entity.Booking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationService {
  Booking makeReservation(Booking booking) throws UserAlreadyHasBookingException;
  boolean cancelReservation(Long bookingId);

  Booking updateReservation(Booking booking,Long bookingId) throws UserAlreadyHasBookingException;

  Optional<List<LocalDate>> getOpenDates(LocalDate beginDate, LocalDate endDate);
}
