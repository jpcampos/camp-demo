package com.jcampos.campdemo.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DateUtils {
  public static List<LocalDate> getDatesBetween(
      LocalDate beginDate, LocalDate endDate) {
    LocalDate endDatePlus1 = endDate.plusDays(1);
    long numOfDaysBetween = ChronoUnit.DAYS.between(beginDate, endDatePlus1);
    return IntStream.iterate(0, i -> i + 1)
        .limit(numOfDaysBetween)
        .mapToObj(i -> beginDate.plusDays(i))
        .collect(Collectors.toList());
  }
}
