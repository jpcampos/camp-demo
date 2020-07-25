package com.jcampos.campdemo.validation;

import com.jcampos.campdemo.model.dto.BookingDto;
import com.jcampos.campdemo.util.MsgKeys;
import java.time.LocalDate;
import java.time.Period;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;


@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
public class AreBookingParamsCorrectValidator implements ConstraintValidator<AreBookingsParamsCorrect, BookingDto> {

  @Autowired
  private MessageSource messageSource;
  public static final String ARRIVAL_DATE = "arrivalDate";
  public static final String DEPARTURE_DATE = "departureDate";
  public static final LocalDate DAYS_30_FROM_TODAY = LocalDate.now().plusDays(30);

  @Override
  public boolean isValid(BookingDto bookingDto,
      ConstraintValidatorContext context) {
    if (bookingDto != null){
      LocalDate arrivalDate = bookingDto.getArrivalDate();
      if (arrivalDate != null){
        if(arrivalDate.isBefore(LocalDate.now()) || arrivalDate.isAfter(LocalDate.now().plusDays(29))){
          buildErrorContext(context, MsgKeys.ARRIVAL_DATE_INCORRECT, ARRIVAL_DATE);
          return false;
        }
      }
      LocalDate departureDate = bookingDto.getDepartureDate();
      if (departureDate != null){
        if(departureDate.isBefore(LocalDate.now().plusDays(1)) || departureDate.isAfter(DAYS_30_FROM_TODAY)){
          buildErrorContext(context, MsgKeys.DEPARTURE_DATE_INCORRECT, DEPARTURE_DATE);
          return false;
        }
      }
      if (arrivalDate != null && departureDate != null ){
        if (arrivalDate.isAfter(departureDate) || arrivalDate.isEqual(departureDate)) {
          buildErrorContext(context, MsgKeys.ADATE_BEFORE_EQUAL_DDATE, ARRIVAL_DATE);
          return false;
        } else if (Period.between(arrivalDate,departureDate).getDays()<1 || Period.between(arrivalDate,departureDate).getDays()>3){
          buildErrorContext(context, MsgKeys.INCORRECT_PERIOD, ARRIVAL_DATE);
          return false;
        }
      }
    }
    return true;
  }

  private void buildErrorContext(ConstraintValidatorContext context, String dateIncorrect,
      String date) {
    context.disableDefaultConstraintViolation();
    context
        .buildConstraintViolationWithTemplate(messageSource.getMessage(dateIncorrect, null,
            LocaleContextHolder.getLocale()))
        .addPropertyNode(date).addConstraintViolation();
  }

}
