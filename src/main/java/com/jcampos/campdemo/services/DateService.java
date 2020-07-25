package com.jcampos.campdemo.services;

import com.jcampos.campdemo.model.entity.Booking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DateService {

  Optional<List<LocalDate>> calculateOpenDates(LocalDate arrivalDate, LocalDate departureDate,
      List<Booking> activeBookingsBetweenDatesList);

  Optional<List<LocalDate>> getOpenDates(LocalDate arrivalDate, LocalDate departureDate);

  Optional<List<LocalDate>> getOpenDatesForExistingBooking(LocalDate arrivalDate, LocalDate departureDate, Long bookingId);
}
