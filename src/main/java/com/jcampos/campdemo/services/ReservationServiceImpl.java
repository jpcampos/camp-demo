package com.jcampos.campdemo.services;

import com.jcampos.campdemo.model.entity.Booking;
import com.jcampos.campdemo.model.enums.BookingStatus;
import com.jcampos.campdemo.model.repository.BookingRepository;
import com.jcampos.campdemo.util.DateUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService{

  final
  BookingRepository bookingRepository;

  final
  DateService dateService;

  final MessageSource messageSource;

  private static final Logger LOGGER = LogManager.getLogger(ReservationServiceImpl.class);


  public ReservationServiceImpl(
      BookingRepository bookingRepository,
      DateService dateService,
      MessageSource messageSource) {
    this.bookingRepository = bookingRepository;
    this.dateService = dateService;
    this.messageSource = messageSource;
  }



  @Override
  public Booking makeReservation(Booking booking){

    Optional<List<LocalDate>> openDates = dateService
        .getOpenDates(booking.getArrivalDate(), booking.getDepartureDate());
    if (openDates.isPresent()) {
      List<LocalDate> requestedDatesList = DateUtils
          .getDatesBetween(booking.getArrivalDate(), booking.getDepartureDate());
      if (openDates.get().containsAll(requestedDatesList)) {
        if (bookingRepository.existsByFirstNameAndLastNameAndEmail(
            booking.getFirstName(),
            booking.getLastName(),
            booking.getEmail())) {
          booking.setBookingStatus(BookingStatus.GUEST_EXISTS);
          return booking;
        } else {
          booking.setBookingStatus(BookingStatus.ACTIVE);
          booking = bookingRepository.save(booking);
        }
      } else{
          booking.setBookingStatus(BookingStatus.DATES_UNAVAILABLE);
          return booking;
        }
    }
    return booking;
  }

  @Override
  public boolean cancelReservation(Long bookingId) {
    Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
    if (existingBooking.isPresent()){
      Booking booking = existingBooking.get();
      bookingRepository.delete(booking);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Booking updateReservation(Booking updatingBooking,Long bookingId){
    Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
    if (existingBooking.isPresent()) {
      Optional<List<LocalDate>> openDates = dateService
          .getOpenDatesForExistingBooking(updatingBooking.getArrivalDate(), updatingBooking.getDepartureDate(),bookingId);
      if (openDates.isPresent()) {
        List<LocalDate> requestedDatesList = DateUtils
            .getDatesBetween(updatingBooking.getArrivalDate(), updatingBooking.getDepartureDate());
        if (openDates.get().containsAll(requestedDatesList)){
            updatingBooking.setBookingStatus(BookingStatus.ACTIVE);
            bookingRepository.save(updatingBooking);
        } else {
          updatingBooking.setBookingStatus(BookingStatus.DATES_UNAVAILABLE);
        }
      }
    }
    return updatingBooking;
  }

  @Override
  public Optional<List<LocalDate>> getOpenDates(LocalDate arrivalDate, LocalDate departureDate) {
    return dateService.getOpenDates(arrivalDate,departureDate);
  }
}
