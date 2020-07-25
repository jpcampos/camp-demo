package com.jcampos.campdemo.jpa.repository


import com.jcampos.campdemo.model.entity.Booking
import com.jcampos.campdemo.model.enums.BookingStatus
import com.jcampos.campdemo.model.repository.BookingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

@ActiveProfiles(profiles = "test")
@DataJpaTest
class BookingRepositorySpec extends Specification {

    @Autowired
    private BookingRepository bookingRepository



    def "Test simple Booking"() {
        def savedBooking = bookingRepository.save(new Booking(
                firstName: "John",
                lastName: "Doe",
                email:"johndoe@email.com",
                arrivalDate: LocalDate.now(),
                departureDate: LocalDate.now().plusDays(1),
                bookingStatus: BookingStatus.ACTIVE))
        def foundBooking = bookingRepository.findById(savedBooking.getId()).get().getId()
        expect:
        savedBooking.getId() == foundBooking
    }

    @Unroll
    def "A booking is made for today. Test that when a search is made for arrivalDate: #arrivalDate then booking found = #result)"() {
         def savedBooking = bookingRepository.save(new Booking(
                 firstName: "John",
                 lastName: "Doe",
                 email:"johndoe@email.com",
                arrivalDate: LocalDate.now(),
                 bookingStatus: BookingStatus.ACTIVE,
                departureDate: LocalDate.now().plusDays(1)))
        def booking = bookingRepository.findBookingByArrivalDate(arrivalDate)
        def actual = booking != null
        expect:""
        actual == result
        where:""
        arrivalDate     | result
        LocalDate.now() | true
        LocalDate.now().plusDays(1) | false
    }



    def "Test making a booking with an already existing booking on the same date throws a data integrity violation"() {
        given:"A customer books a reservation on a given date (today + 1 day)"
        def savedBooking = bookingRepository.save(new Booking(
                firstName: "John",
                lastName: "Doe",
                email:"johndoe@email.com",
                bookingStatus: BookingStatus.ACTIVE,
                arrivalDate: LocalDate.now().plusDays(1),
                departureDate: LocalDate.now().plusDays(1)))
        when:"A customer tries to make a reservation on the same date (today + 1 day)"
        def secondSavedBooking = bookingRepository.save(new Booking(
                firstName: "John",
                lastName: "Doe",
                email:"johndoe@email.com",
                bookingStatus: BookingStatus.ACTIVE,
                arrivalDate: LocalDate.now().plusDays(1),
                departureDate: LocalDate.now().plusDays(2)))
        then:"The constraint that the arrival date is unique is violated"
        thrown(DataIntegrityViolationException)
    }

    @Unroll
    def "Test. When customer: 'Jane Doe janedoe@email.com' exists in db and search for  firstName=#firstName, lastName=#lastName, email = #email results in foundCustomer = #customerIsPresent"(){
        def savedBooking = bookingRepository.save(new Booking(
                firstName: "Jane",
                lastName: "Doe",
                email:"janedoe@email.com",
                bookingStatus: BookingStatus.ACTIVE,
                arrivalDate: LocalDate.now().plusDays(1),
                departureDate: LocalDate.now().plusDays(1)))
         def retrievedCustomer = bookingRepository.findBookingsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndEmailIgnoreCase(firstName, lastName, email)
        def actual = retrievedCustomer != null && !retrievedCustomer.isEmpty()
        expect:""
        customerIsPresent == actual
        where:"Test values"
        firstName | lastName | email               | customerIsPresent
        "JANE"    | "DOE"     | "JANEDOE@EMAIL.COM" | true
        "jane"    | "doe"     | "janedoe@email.com" | true
        "jane"    | "DOE"     | "JANEDOE@EMAIL.COM" | true
        "JANE"    | "doe"     | "JANEDOE@EMAIL.COM" | true
        "JANE"    | "DOE"     | "janedoe@email.com" | true
        "jane"    | "DOE"     | "JANEDOE@EMAIL.COM" | true
        "john"    | "doe"     | "janedoe@email.com" | false
        "jane"    | "NOTDOE"  | "janedoe@email.com" | false
        "jane"    | "doe"     | "janedoe"           | false
        "john"    | "notdoe"  | "1janedoe@email.com"| false
    }


    def "Test retrieving all bookings for customer when multiple bookings are made"(){
        def firstBooking = bookingRepository.save(new Booking(
                firstName: "John",
                lastName: "Doe",
                email:"johndoe@email.com",
                bookingStatus: BookingStatus.ACTIVE,
                arrivalDate: LocalDate.now().plusDays(1),
                departureDate: LocalDate.now().plusDays(1)))
        def secondBooking = bookingRepository.save(new Booking(
                firstName: "John",
                lastName: "Doe",
                email:"johndoe@email.com",
                bookingStatus: BookingStatus.ACTIVE,
                arrivalDate: LocalDate.now().plusDays(60),
                departureDate: LocalDate.now().plusDays(61)))
        def bookings = bookingRepository.findBookingsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndEmailIgnoreCase("John","Doe","johndoe@email.com")
        expect:""
        !bookings.isEmpty()
        bookings.size() == 2
    }

    @Unroll
    def "Test finding bookings in between dates"(){
        def day20 = LocalDate.now().plusDays(20)
        def day23 = LocalDate.now().plusDays(23)
        def day26 = LocalDate.now().plusDays(26)
        def day28 = LocalDate.now().plusDays(28)

        bookingRepository.save(new Booking(
                firstName: "John",
                lastName: "Doe",
                email:"johndoe@email.com",
                bookingStatus: BookingStatus.ACTIVE,
                arrivalDate: day20,
                departureDate: day23))

        bookingRepository.save(new Booking(
                firstName: "Jane",
                lastName: "Doe",
                email:"johndoe@email.com",
                bookingStatus: BookingStatus.ACTIVE,
                arrivalDate: day26,
                departureDate: day28))
        def bookings = bookingRepository.findActiveBookingsBetweenDates(day23,day26,BookingStatus.ACTIVE)
        expect:""
        !bookings.isEmpty()
        bookings.size() == 1
        bookings.get(0).getArrivalDate().equals(day20)
        bookings.get(0).getDepartureDate().equals(day23)
    }

    @Unroll
    def "Test finding bookings in between dates and given my bookins present"(){
        def day10 = LocalDate.now().plusDays(10)
        def day13 = LocalDate.now().plusDays(13)
        def day16 = LocalDate.now().plusDays(16)
        def day18 = LocalDate.now().plusDays(18)

        def myBooking = bookingRepository.save(new Booking(
                firstName: "John",
                lastName: "Doe",
                email:"johndoe@email.com",
                bookingStatus: BookingStatus.ACTIVE,
                arrivalDate: day10,
                departureDate: day13))

        bookingRepository.save(new Booking(
                firstName: "Jane",
                lastName: "Doe",
                email:"johndoe@email.com",
                bookingStatus: BookingStatus.ACTIVE,
                arrivalDate: day16,
                departureDate: day18))
        def bookings = bookingRepository.findActiveBookingsBetweenDatesMinusExistingBooking(day13,day16,BookingStatus.ACTIVE,myBooking.getId())
        expect:""
        bookings.isEmpty()
        bookings.size() == 0
    }

}