package com.jcampos.campdemo.validation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { AreBookingParamsCorrectValidator.class })
public @interface AreBookingsParamsCorrect
{
  String message() default "Parameters are incorrect";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

