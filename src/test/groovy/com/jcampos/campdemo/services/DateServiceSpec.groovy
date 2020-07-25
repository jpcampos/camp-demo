package com.jcampos.campdemo.services

import com.jcampos.campdemo.model.entity.Booking
import com.jcampos.campdemo.model.repository.BookingRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDate

@ActiveProfiles(profiles = "test")
@SpringBootTest
class DateServiceSpec extends Specification {

    @SpringBean
    BookingRepository bookingRepository = Mock(BookingRepository)

    @Autowired
    DateService dateService

    def "test get open dates call"() {
        given:"Set up mock dates to call the service"
        def arrivalDate = LocalDate.now()
        def departureDate = LocalDate.now().plusDays(1)
        def mockBooking = Mock(Booking){
            getArrivalDate() >> LocalDate.now().plusDays(2)
            getDepartureDate() >> LocalDate.now().plusDays(3)
        }
        def mockBookingList = [mockBooking]

        when:"Service method is called"
        dateService.getOpenDates(arrivalDate,departureDate)

        then:"The following calls are made"
        1 * bookingRepository.findActiveBookingsBetweenDates(*_) >> mockBookingList
    }

    def "test get open dates for existing booking call"() {
        given:"Set up mock dates to call the service"
        def arrivalDate = LocalDate.now()
        def departureDate = LocalDate.now().plusDays(1)
        def mockBooking = Mock(Booking){
            getArrivalDate() >> LocalDate.now().plusDays(2)
            getDepartureDate() >> LocalDate.now().plusDays(3)
        }
        def mockBookingList = [mockBooking]

        when:"Service method is called"
        dateService.getOpenDatesForExistingBooking(arrivalDate,departureDate,1)

        then:"The following calls are made"
        1 * bookingRepository.findActiveBookingsBetweenDatesMinusExistingBooking(*_) >> mockBookingList
    }

}