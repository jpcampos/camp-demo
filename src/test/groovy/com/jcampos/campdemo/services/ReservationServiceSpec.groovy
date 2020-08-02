package com.jcampos.campdemo.services

import com.jcampos.campdemo.model.entity.Booking
import com.jcampos.campdemo.model.enums.BookingStatus
import com.jcampos.campdemo.model.repository.BookingRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

@ActiveProfiles(profiles = "test")
@SpringBootTest
class ReservationServiceSpec extends Specification {

    @SpringBean
    BookingRepository bookingRepository = Mock(BookingRepository)

    @SpringBean
    DateService dateService = Mock(DateService)

    @Autowired
    MessageSource messageSource

    @Autowired
    ReservationService reservationService

    @Unroll
    def "Test make reservation sends back status #expectedStatus when guest has a reservation already = #guestExists"() {
        given:"set up the mocks"
        dateService.getOpenDates(*_) >> Optional.of(openDates)
        def bookingToMake = new Booking(id:1L,firstName: "John", lastName: "Doe", email:"jdoe@email.com",arrivalDate: openDates.get(0),departureDate: openDates.get(1))
        bookingRepository.save(bookingToMake) >> bookingToMake
        when:"the make reservation method is called"
        def bookingResult = reservationService.makeReservation(bookingToMake)
        then:"The expected values are set"
        bookingResult.getBookingStatus() == expectedStatus
        if (guestExists)
            1 * bookingRepository.existsByFirstNameAndLastNameAndEmail(*_) >> guestExists
        where:"Parameterized table with values"
        bookingDates | openDates | guestExists | expectedStatus
        [LocalDate.now(),LocalDate.now().plusDays(1 as long)]  | [LocalDate.now(),LocalDate.now().plusDays(1 as long)] | true | BookingStatus.GUEST_EXISTS
        [LocalDate.now(),LocalDate.now().plusDays(1 as long)]  | [LocalDate.now(),LocalDate.now().plusDays(1 as long)] | false | BookingStatus.ACTIVE
        [LocalDate.now(),LocalDate.now().plusDays(3 as long)]  | [LocalDate.now(),LocalDate.now().plusDays(4 as long)] | false | BookingStatus.DATES_UNAVAILABLE
    }

    @Unroll
    def "Test update reservation sends back status #expectedStatus when guest has a reservation already "() {
        given:"set up the mocks"
        dateService.getOpenDatesForExistingBooking(*_) >> Optional.of(openDates)
        def bookingToMake = new Booking(id:1L,firstName: "John", lastName: "Doe", email:"jdoe@email.com",arrivalDate: bookingDates.get(0),departureDate: bookingDates.get(1))
        bookingRepository.save(bookingToMake) >> bookingToMake
        when:"the make reservation method is called"
        def bookingResult = reservationService.updateReservation(bookingToMake,1L)
        then:"The expected values are set"
        bookingResult.getBookingStatus() == expectedStatus
        1 * bookingRepository.findById(1L) >> Optional.of(bookingToMake)
        where:"Parameterized table with values"
        bookingDates | openDates |  expectedStatus
        [LocalDate.now(),LocalDate.now().plusDays(1 as long)]  | [LocalDate.now(),LocalDate.now().plusDays(1 as long)] | BookingStatus.ACTIVE
        [LocalDate.now(),LocalDate.now().plusDays(3 as long)]  | [LocalDate.now(),LocalDate.now().plusDays(4 as long)] | BookingStatus.DATES_UNAVAILABLE
    }

    def "test getOpenDates"() {
        given:"an arrival and departure date"
        def arrivalDate = LocalDate.now()
        def departureDate = LocalDate.now().plusDays(1)
        when:"reservation service get open dates is called"
        reservationService.getOpenDates(arrivalDate,departureDate)
        then:"date service is called with the same dates"
        1 * dateService.getOpenDates(arrivalDate,departureDate)
    }


}