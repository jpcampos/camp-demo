package com.jcampos.campdemo.model.repository;

import com.jcampos.campdemo.model.entity.Booking;
import com.jcampos.campdemo.model.enums.BookingStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

  Booking findBookingByArrivalDate(LocalDate arrivalDate);


  boolean existsByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email);

  List<Booking> findBookingsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndEmailIgnoreCase(String firstName, String lastName, String email);

  @Query("SELECT b from Booking b where b.arrivalDate >= :beginDate AND b.departureDate < :endDate" +
         " OR (b.departureDate >= :beginDate AND b.departureDate <= :endDate)" +
         " OR (:beginDate = :endDate AND b.arrivalDate <= :beginDate AND b.departureDate >= :beginDate)" +
         " AND b.bookingStatus = :bookingStatus"
  )
  List<Booking> findActiveBookingsBetweenDates(@Param("beginDate") LocalDate beginDate,@Param("endDate")LocalDate endDate,
      @Param("bookingStatus") BookingStatus bookingStatus);

  @Query("SELECT b from Booking b where b.id <>:bookingId AND (b.arrivalDate >= :beginDate AND b.departureDate < :endDate" +
      " OR (b.departureDate >= :beginDate AND b.departureDate <= :endDate)" +
      " OR (:beginDate = :endDate AND b.arrivalDate <= :beginDate AND b.departureDate >= :beginDate)" +
      " AND b.bookingStatus = :bookingStatus)"
  )
  List<Booking> findActiveBookingsBetweenDatesMinusExistingBooking(@Param("beginDate") LocalDate beginDate,@Param("endDate")LocalDate endDate,
      @Param("bookingStatus") BookingStatus bookingStatus, @Param("bookingId")Long bookingId);
}
