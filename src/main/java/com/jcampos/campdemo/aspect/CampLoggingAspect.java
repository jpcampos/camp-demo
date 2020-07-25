package com.jcampos.campdemo.aspect;

import com.jcampos.campdemo.util.MsgKeys;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CampLoggingAspect {

  private static final Logger LOGGER = LogManager.getLogger(CampLoggingAspect.class);




  private final MessageSource messageSource;

  public CampLoggingAspect(MessageSource messageSource) {
    this.messageSource = messageSource;
  }


  //Pointcut to match repositories, services and controllers
  @Pointcut("within(@org.springframework.stereotype.Repository *)" +
      " || within(@org.springframework.stereotype.Service *)" +
      " || within(@org.springframework.web.bind.annotation.RestController *)")
  public void springComponentsPointcut() {
  }

  //Pointcut for all the spring beans in the main context
  @Pointcut("within(com.jcampos.campdemo..*)" +
      " || within(com.jcampos.campdemo.services..*)" +
      " || within(com.jcampos.campdemo.model..*)" +
      " || within(com.jcampos.campdemo.controller..*)")
  public void campDemoPointcut() {
  }

  /**
   * Advice that will log necessary information after exceptions are thrown
   *
   * @param joinPoint join point for advice
   * @param e exception
   */
  @AfterThrowing(pointcut = "springComponentsPointcut() && campDemoPointcut()", throwing = "e")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
    LOGGER.error(messageSource.getMessage(MsgKeys.EXCEPTION_POINTCUT,null,
        LocaleContextHolder.getLocale()), joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : messageSource.getMessage(MsgKeys.NULL,null,LocaleContextHolder.getLocale()));
  }

  /**
   * Advice to log information around a method entry and exit
   *
   * @param joinPoint join point for advice
   * @return result
   * @throws Throwable throws IllegalArgumentException
   */
  @Around("springComponentsPointcut() && campDemoPointcut() ")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(messageSource.getMessage(MsgKeys.ENTRY_TO_METHOD_WITH_ARGUMENTS,null,LocaleContextHolder.getLocale()), joinPoint.getSignature().getDeclaringTypeName(),
          joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }
    try {
      Object result = joinPoint.proceed();
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(messageSource.getMessage(MsgKeys.EXIT_OF_METHOD_WITH_RESULT,null,LocaleContextHolder.getLocale()), joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(), result);
      }
      return result;
    } catch (IllegalArgumentException e) {
      LOGGER.error(messageSource.getMessage(MsgKeys.ILLEGAL_ARGUMENT_EXCEPTION_IN,null,LocaleContextHolder.getLocale()), Arrays.toString(joinPoint.getArgs()),
          joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
      throw e;
    }
  }
}
