package com.jcampos.campdemo.services;

import com.jcampos.campdemo.model.entity.Booking;
import com.jcampos.campdemo.model.enums.BookingStatus;
import com.jcampos.campdemo.model.repository.BookingRepository;
import com.jcampos.campdemo.util.DateUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DateServiceImpl implements DateService{

  private final BookingRepository bookingRepository;

  public DateServiceImpl(BookingRepository bookingRepository){
    this.bookingRepository = bookingRepository;
  }

  @Override
  public Optional<List<LocalDate>> getOpenDates(LocalDate arrivalDate, LocalDate departureDate) {
    List<Booking> activeBookingsBetweenDatesList = bookingRepository
        .findActiveBookingsBetweenDates(arrivalDate, departureDate, BookingStatus.ACTIVE);
    return calculateOpenDates(arrivalDate, departureDate,
        activeBookingsBetweenDatesList);
  }

  @Override
  public Optional<List<LocalDate>> getOpenDatesForExistingBooking(LocalDate arrivalDate,
      LocalDate departureDate, Long bookingId) {
    List<Booking> activeBookingsBetweenDatesList = bookingRepository
        .findActiveBookingsBetweenDatesMinusExistingBooking(arrivalDate, departureDate, BookingStatus.ACTIVE,bookingId);
    return calculateOpenDates(arrivalDate, departureDate,
        activeBookingsBetweenDatesList);
  }

  @Override
  public Optional<List<LocalDate>> calculateOpenDates(LocalDate arrivalDate,
      LocalDate departureDate,
      List<Booking>activeBookingsBetweenDatesList) {
    arrivalDate = arrivalDate == null?LocalDate.now():arrivalDate;
    departureDate = departureDate == null?LocalDate.now().plusDays(30):departureDate;
    List<LocalDate> requestedDatesList = DateUtils.getDatesBetween(arrivalDate, departureDate);

    if (!activeBookingsBetweenDatesList.isEmpty()) {
      List<LocalDate> bookedDates = new ArrayList<>();
      StreamEx.of(activeBookingsBetweenDatesList)
          .forEachOrdered(booking -> bookedDates.addAll(DateUtils.getDatesBetween(booking.getArrivalDate(),booking.getDepartureDate().minusDays(1))));
      requestedDatesList.removeAll(bookedDates);
    }
    return Optional.of(requestedDatesList);
  }

}
