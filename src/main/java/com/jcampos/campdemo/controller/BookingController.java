package com.jcampos.campdemo.controller;

import com.jcampos.campdemo.exception.DatesAlreadyBookedException;
import com.jcampos.campdemo.exception.GetDatesException;
import com.jcampos.campdemo.exception.UserAlreadyHasBookingException;
import com.jcampos.campdemo.model.dto.BookingDto;
import com.jcampos.campdemo.model.entity.Booking;
import com.jcampos.campdemo.model.enums.BookingStatus;
import com.jcampos.campdemo.services.ReservationService;
import com.jcampos.campdemo.util.MsgKeys;
import com.jcampos.campdemo.model.dto.SearchDates;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MsgKeys.CAMPDEMO)
public class BookingController {



  private final ReservationService reservationService;
  private final MessageSource messageSource;

  @Autowired
  public BookingController(
      ReservationService reservationService,
      MessageSource messageSource) {
    this.reservationService = reservationService;
    this.messageSource = messageSource;;
  }

  @GetMapping(value= MsgKeys.BOOKING_DATES_RESOURCE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<LocalDate>> listAvailableDates(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate arrivalDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate departureDate) {
    Set<ConstraintViolation<SearchDates>> constraintViolations = checkConstraintValidationExceptions(
        arrivalDate, departureDate);
    if (!constraintViolations.isEmpty()){
      AtomicReference<String> message = new AtomicReference<>("");
      constraintViolations.forEach(searchDatesConstraintViolation ->
        message.getAndSet(message.get() + "" + messageSource.getMessage(searchDatesConstraintViolation.getMessage(),null,LocaleContextHolder.getLocale())));
      throw new GetDatesException(message.get());
    }
    Optional<List<LocalDate>> openDates = reservationService.getOpenDates(arrivalDate, departureDate);
    return openDates.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  private Set<ConstraintViolation<SearchDates>> checkConstraintValidationExceptions(LocalDate arrivalDate,LocalDate departureDate) {
    SearchDates searchDates = new SearchDates(arrivalDate,departureDate);
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    return validator.validate( searchDates );
  }

  @PostMapping(value = MsgKeys.BOOKING_RESOURCE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> makeBooking(@Valid @RequestBody BookingDto bookingDto){
    ModelMapper modelMapper = new ModelMapper();
    Booking entityBooking = modelMapper.map(bookingDto, Booking.class);
    entityBooking = reservationService
        .makeReservation(entityBooking);
    if (entityBooking.getId()!= null) {
      return new ResponseEntity<>(entityBooking.getId(), HttpStatus.CREATED);
    } else {
      checkReservationExceptions(entityBooking);
    }
    return  new ResponseEntity<>(entityBooking.getId(), HttpStatus.BAD_REQUEST);
  }

  @PutMapping(value = MsgKeys.UPDATE_CANCEL_BOOKING_RESOURCE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> updateBooking(@Valid @RequestBody BookingDto bookingDto,@PathVariable Long id){
    ModelMapper modelMapper = new ModelMapper();
    Booking entityBooking = modelMapper.map(bookingDto, Booking.class);
    entityBooking = reservationService.updateReservation(entityBooking,id);
    if (BookingStatus.ACTIVE == entityBooking.getBookingStatus()){
      return new ResponseEntity<>(entityBooking.getId(),HttpStatus.OK);
    } else {
      checkReservationExceptions(entityBooking);
    }
    return new ResponseEntity<>(entityBooking.getId(),HttpStatus.EXPECTATION_FAILED);
  }

  @DeleteMapping(value = MsgKeys.UPDATE_CANCEL_BOOKING_RESOURCE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
    boolean isCancelled = reservationService.cancelReservation(id);
    if (!isCancelled) {
      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  private void checkReservationExceptions(Booking entityBooking) {
    if (BookingStatus.GUEST_EXISTS == entityBooking.getBookingStatus()) {
      throw new UserAlreadyHasBookingException(
          entityBooking.getFirstName() + " " +
              entityBooking.getLastName() + " " +
              entityBooking.getEmail() + " " +
              messageSource.getMessage(MsgKeys.GUEST_ALREADY_HAS_BOOKING, null,
                  LocaleContextHolder.getLocale()));
    } else if (BookingStatus.DATES_UNAVAILABLE == entityBooking.getBookingStatus()) {
      throw new DatesAlreadyBookedException(
          entityBooking.getArrivalDate() + " " +
              entityBooking.getDepartureDate() + " " +
              messageSource.getMessage(MsgKeys.DATES_ALREADY_BOOKED, null,
                  LocaleContextHolder.getLocale()));
    }
  }



}
