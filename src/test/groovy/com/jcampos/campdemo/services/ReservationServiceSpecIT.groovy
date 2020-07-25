package com.jcampos.campdemo.services

import com.jcampos.campdemo.model.entity.Booking
import com.jcampos.campdemo.model.enums.BookingStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDate

@ActiveProfiles(profiles = "test")
@SpringBootTest
class ReservationServiceSpecIT extends Specification {

    @Autowired
    ReservationService reservationService


    def "Test exception thrown when trying to make booking on the same arrival date"() {
        given:"Two reservations with the same arrival date will be made into the system"
        def booking = new Booking(
                firstName: "John",
                lastName: "Doe",
                email:"johndoe@email.com",
                arrivalDate: LocalDate.now(),
                departureDate: LocalDate.now().plusDays(1),
                bookingStatus: BookingStatus.ACTIVE)
        def booking2 = new Booking(
                firstName: "Jane",
                lastName: "Doe",
                email:"janedoe@email.com",
                arrivalDate: LocalDate.now(),
                departureDate: LocalDate.now().plusDays(1),
                bookingStatus: BookingStatus.ACTIVE)
        when: "Both reservations are attempted"
        def madeReservation = reservationService.makeReservation(booking)
        def madeReservation2 = reservationService.makeReservation(booking2)
        then:"madeReservation 2 is null"
        madeReservation2.id == null
    }

    def "Test reservation can be cancelled"() {
        given:"A reservation is saved into the sytem"
        def booking = new Booking(
                firstName: "Mike",
                lastName: "Doe",
                email:"johndoe@email.com",
                arrivalDate: LocalDate.now().plusDays(18),
                departureDate: LocalDate.now().plusDays(19),
                bookingStatus: BookingStatus.ACTIVE)
        def madeBooking = reservationService.makeReservation(booking)
        and:" The reservation id is used to cancel the reservation"
        expect:"The reservation service method cancel reservation returns true for cancelling the reservation"
        reservationService.cancelReservation(madeBooking.getId())
    }

    def "Test reservation id does not exist therefore cannot be cancelled"() {
        expect:"A non existent reservation id cannot be cancelled and returns false"
        !reservationService.cancelReservation(34934934734904L)
    }

}