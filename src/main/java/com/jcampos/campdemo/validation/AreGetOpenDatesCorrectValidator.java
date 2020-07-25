package com.jcampos.campdemo.validation;

import com.jcampos.campdemo.model.dto.SearchDates;
import com.jcampos.campdemo.util.MsgKeys;
import java.time.LocalDate;
import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
@Component
public class AreGetOpenDatesCorrectValidator implements ConstraintValidator<AreGetOpenDatesCorrect, SearchDates> {

  public static final String BEGIN_DATE = "arrivalDate";
  public static final String END_DATE = "departureDate";

  public static final LocalDate DAYS_30_FROM_TODAY = LocalDate.now().plusDays(30);



  @Override
  public boolean isValid(SearchDates searchDates,
      ConstraintValidatorContext context) {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setCacheSeconds(5);
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setFallbackToSystemLocale(true);
    messageSource.setUseCodeAsDefaultMessage(true);
    messageSource.setBasenames(String.valueOf(Arrays.asList("classpath:")));
    if (searchDates != null){
      LocalDate beginDate = searchDates.getArrivalDate();
        if(beginDate.isBefore(LocalDate.now()) || beginDate.isAfter(LocalDate.now().plusDays(29))){
          context.disableDefaultConstraintViolation();
          context.buildConstraintViolationWithTemplate(MsgKeys.ARRIVAL_DATE_INCORRECT)
              .addPropertyNode(BEGIN_DATE).addConstraintViolation();
          return false;
      }
      LocalDate endDate = searchDates.getDepartureDate();
        if(endDate.isBefore(LocalDate.now().plusDays(1)) || endDate.isAfter(DAYS_30_FROM_TODAY)){
          context.disableDefaultConstraintViolation();
          context.buildConstraintViolationWithTemplate(MsgKeys.DEPARTURE_DATE_INCORRECT)
              .addPropertyNode(END_DATE).addConstraintViolation();
          return false;
        }
        if (beginDate.isAfter(endDate) || beginDate.isEqual(endDate)){
          context.disableDefaultConstraintViolation();
          context.buildConstraintViolationWithTemplate(MsgKeys.ADATE_BEFORE_EQUAL_DDATE)
              .addPropertyNode(BEGIN_DATE)
              .addConstraintViolation();
          return false;
        }
    }
    return true;
  }
}
