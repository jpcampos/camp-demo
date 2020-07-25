package com.jcampos.campdemo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.jcampos.campdemo.model.dto.BookingDto
import com.jcampos.campdemo.model.dto.SearchDates
import com.jcampos.campdemo.model.entity.Booking
import com.jcampos.campdemo.model.enums.BookingStatus
import com.jcampos.campdemo.services.ReservationService
import com.jcampos.campdemo.util.MsgKeys
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.repository.query.Parameters
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@ActiveProfiles(profiles = "test")
@WebMvcTest(controllers = BookingController.class)
class BookingControllerSpec extends Specification {
    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    @SpringBean
    ReservationService reservationService = Mock(ReservationService)

    @Unroll
    def "when get open dates is performed with incorrect arrival date or departure date then the response has status 400 BAD request and content contains the arrival date or departure date incorrect error message"() {

        reservationService.getOpenDates(*_) >> Optional.of([LocalDate.of(2020, 07, 15)])
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>()
        params.add("arrivalDate", arrivalDate.format(DateTimeFormatter.ISO_DATE))
        params.add("departureDate", departureDate.format(DateTimeFormatter.ISO_DATE))
        def response = mockMvc.perform((get(MsgKeys.CAMPDEMO + MsgKeys.BOOKING_DATES_RESOURCE))
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andReturn().getResponse()
        expect: "Status is expectedStatus and the response contains information about the arrival date'"
        if (expectedStatus == HttpStatus.BAD_REQUEST.value())
            response.contentAsString.contains(ResourceBundle.getBundle("messages").getString(messageKey))
        expectedStatus == response.status
        where: "Values for json request and expected responses"
        arrivalDate                   | departureDate                | expectedStatus                 | messageKey
        LocalDate.now().plusDays(-10) | LocalDate.now().plusDays(-8) | HttpStatus.BAD_REQUEST.value() | MsgKeys.ARRIVAL_DATE_INCORRECT
        LocalDate.now()               | LocalDate.now().plusDays(-8) | HttpStatus.BAD_REQUEST.value() | MsgKeys.DEPARTURE_DATE_INCORRECT
        LocalDate.now()               | LocalDate.now()              | HttpStatus.BAD_REQUEST.value() | MsgKeys.DEPARTURE_DATE_INCORRECT
        LocalDate.now().plusDays(30)  | LocalDate.now().plusDays(31) | HttpStatus.BAD_REQUEST.value() | MsgKeys.ARRIVAL_DATE_INCORRECT
        LocalDate.now().plusDays(1)   | LocalDate.now().plusDays(31) | HttpStatus.BAD_REQUEST.value() | MsgKeys.DEPARTURE_DATE_INCORRECT
        LocalDate.now().plusDays(29)  | LocalDate.now().plusDays(29) | HttpStatus.BAD_REQUEST.value() | MsgKeys.ADATE_BEFORE_EQUAL_DDATE
        LocalDate.now().plusDays(1)  | LocalDate.now().plusDays(2)   | HttpStatus.OK.value()     | MsgKeys.ADATE_BEFORE_EQUAL_DDATE
    }

    @Unroll
    def "when get open dates is performed with correct arrivalDate #arrivalDate and departure date #departureDate the status is  #expectedStatus"() {
        reservationService.getOpenDates(*_) >> Optional.of([LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)])
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>()
        params.add("arrivalDate", arrivalDate.format(DateTimeFormatter.ISO_DATE))
        params.add("departureDate", departureDate.format(DateTimeFormatter.ISO_DATE))
        def response = mockMvc.perform((get(MsgKeys.CAMPDEMO + MsgKeys.BOOKING_DATES_RESOURCE))
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andReturn().getResponse()
        expect: "Status is expectedStatus '"
        expectedStatus == response.status
        where: "Values for json request and expected responses"
        arrivalDate                   | departureDate                | expectedStatus
        LocalDate.now().plusDays(1) | LocalDate.now().plusDays(2) | HttpStatus.OK.value()
        LocalDate.now().plusDays(1) | LocalDate.now().plusDays(3) | HttpStatus.OK.value()
      }


    def "when update booking is not active and does not throw exceptions it will return an expectation failed response"() {
        given: "A booking dto json object"
        def bookingDto = new BookingDto(
                firstName: "John",
                lastName: "Doe",
                email: "jdoe@email.com",
                arrivalDate: LocalDate.now(),
                departureDate: LocalDate.now().plusDays(1)
        )
        def jsonContent = new ObjectMapper().writeValueAsString(bookingDto)
        when:"The update method is called"
        reservationService.updateReservation(*_) >> new Booking(id: 1,bookingStatus: BookingStatus.CANCELLED)
        def response = mockMvc.perform((put(MsgKeys.CAMPDEMO + MsgKeys.BOOKING_RESOURCE+"/1")).contentType(MediaType.APPLICATION_JSON).content(jsonContent)).andReturn().getResponse()
        then: "Status is Expectation failed"
        HttpStatus.EXPECTATION_FAILED.value() == response.status
    }

    def "when save booking is not active and does not throw exceptions it will return an expectation failed response"() {
        given: "A booking dto json object"
        def bookingDto = new BookingDto(
                firstName: "John",
                lastName: "Doe",
                email: "jdoe@email.com",
                arrivalDate: LocalDate.now(),
                departureDate: LocalDate.now().plusDays(1)
        )
        def jsonContent = new ObjectMapper().writeValueAsString(bookingDto)
        when:"The make reservation method is called"
        reservationService.makeReservation(*_) >> new Booking(id: null,bookingStatus: BookingStatus.CANCELLED)
        def response = mockMvc.perform((post(MsgKeys.CAMPDEMO + MsgKeys.BOOKING_RESOURCE)).contentType(MediaType.APPLICATION_JSON).content(jsonContent)).andReturn().getResponse()
        then: "Status is Expectation failed"
        HttpStatus.BAD_REQUEST.value() == response.status
    }



    @Unroll
    def "when make a booking is performed with incorrect arrival date #arrivalDate or departure date#departureDate then the response has status 400 BAD request and error message is #messageKey"() {
        given: "A booking dto json object"
        def bookingDto = new BookingDto(
                firstName: "John",
                lastName: "Doe",
                email: "jdoe@email.com",
                arrivalDate: arrivalDate,
                departureDate: departureDate
        )
        def jsonContent = new ObjectMapper().writeValueAsString(bookingDto)
        reservationService.makeReservation(bookingDto) >> new Booking(id: 1)
        expect: "Status is 400 and the response contains incorrect arrival date'"
        def response = mockMvc.perform((post(MsgKeys.CAMPDEMO + MsgKeys.BOOKING_RESOURCE)).contentType(MediaType.APPLICATION_JSON).content(jsonContent)).andReturn().getResponse()
        response.contentAsString.contains(ResourceBundle.getBundle("messages").getString(messageKey))
        expectedStatus == response.status
        where: "Values for json request and expected responses"
        arrivalDate                  | departureDate                | expectedStatus                 | messageKey
        LocalDate.of(2019, 02, 01)   | LocalDate.of(2019, 02, 01)   | HttpStatus.BAD_REQUEST.value() | MsgKeys.ARRIVAL_DATE_INCORRECT
        LocalDate.now()              | LocalDate.of(2019, 02, 01)   | HttpStatus.BAD_REQUEST.value() | MsgKeys.DEPARTURE_DATE_INCORRECT
        LocalDate.now()              | LocalDate.now()              | HttpStatus.BAD_REQUEST.value() | MsgKeys.DEPARTURE_DATE_INCORRECT
        LocalDate.now().plusDays(30) | LocalDate.now().plusDays(31) | HttpStatus.BAD_REQUEST.value() | MsgKeys.ARRIVAL_DATE_INCORRECT
        LocalDate.now().plusDays(1)  | LocalDate.now().plusDays(31) | HttpStatus.BAD_REQUEST.value() | MsgKeys.DEPARTURE_DATE_INCORRECT
        LocalDate.now().plusDays(29) | LocalDate.now().plusDays(29) | HttpStatus.BAD_REQUEST.value() | MsgKeys.ADATE_BEFORE_EQUAL_DDATE
        LocalDate.now().plusDays(2)  | LocalDate.now().plusDays(7)  | HttpStatus.BAD_REQUEST.value() | MsgKeys.INCORRECT_PERIOD
    }

    @Unroll
    def "when make a booking is performed with the correct arrival date #arrivalDate or departure date#departureDate then the response has status 400 BAD request and error message is #messageKey"() {
        given: "A booking dto json object"
        def bookingDto = new BookingDto(
                firstName: "John",
                lastName: "Doe",
                email: "jdoe@email.com",
                arrivalDate: arrivalDate,
                departureDate: departureDate,
        )
        def jsonContent = new ObjectMapper().writeValueAsString(bookingDto)
        reservationService.makeReservation(_ as Booking) >> new Booking(id: 1)
        def response = mockMvc.perform((post(MsgKeys.CAMPDEMO + MsgKeys.BOOKING_RESOURCE)).contentType(MediaType.APPLICATION_JSON).content(jsonContent)).andReturn().getResponse()
        expect: "Status is 201 (Created) and the response contains the booking id'"
        response.contentAsString.contains("1")
        response.status == HttpStatus.CREATED.value()
        where: "Values for json request and expected responses"
        arrivalDate                 | departureDate
        LocalDate.now().plusDays(1) | LocalDate.now().plusDays(2)
        LocalDate.now().plusDays(1) | LocalDate.now().plusDays(3)
        LocalDate.now().plusDays(1) | LocalDate.now().plusDays(4)
        LocalDate.now().plusDays(2) | LocalDate.now().plusDays(3)
        LocalDate.now().plusDays(2) | LocalDate.now().plusDays(4)
        LocalDate.now().plusDays(2) | LocalDate.now().plusDays(5)
        LocalDate.now().plusDays(3) | LocalDate.now().plusDays(4)
        LocalDate.now().plusDays(3) | LocalDate.now().plusDays(5)
        LocalDate.now().plusDays(3) | LocalDate.now().plusDays(6)
        LocalDate.now().plusDays(4) | LocalDate.now().plusDays(5)
        LocalDate.now().plusDays(4) | LocalDate.now().plusDays(6)
        LocalDate.now().plusDays(4) | LocalDate.now().plusDays(7)
        LocalDate.now().plusDays(5) | LocalDate.now().plusDays(6)
        LocalDate.now().plusDays(5) | LocalDate.now().plusDays(7)
        LocalDate.now().plusDays(5) | LocalDate.now().plusDays(8)
        LocalDate.now().plusDays(6) | LocalDate.now().plusDays(7)
        LocalDate.now().plusDays(6) | LocalDate.now().plusDays(8)
        LocalDate.now().plusDays(6) | LocalDate.now().plusDays(9)
        LocalDate.now().plusDays(7) | LocalDate.now().plusDays(8)
        LocalDate.now().plusDays(7) | LocalDate.now().plusDays(9)
        LocalDate.now().plusDays(7) | LocalDate.now().plusDays(10)
        LocalDate.now().plusDays(8) | LocalDate.now().plusDays(9)
        LocalDate.now().plusDays(8) | LocalDate.now().plusDays(10)
        LocalDate.now().plusDays(8) | LocalDate.now().plusDays(11)
        LocalDate.now().plusDays(9) | LocalDate.now().plusDays(10)
        LocalDate.now().plusDays(9) | LocalDate.now().plusDays(11)
        LocalDate.now().plusDays(9) | LocalDate.now().plusDays(12)
        LocalDate.now().plusDays(10)| LocalDate.now().plusDays(11)
        LocalDate.now().plusDays(10)| LocalDate.now().plusDays(12)
        LocalDate.now().plusDays(10)| LocalDate.now().plusDays(13)
        LocalDate.now().plusDays(11)| LocalDate.now().plusDays(12)
        LocalDate.now().plusDays(11)| LocalDate.now().plusDays(13)
        LocalDate.now().plusDays(11)| LocalDate.now().plusDays(14)
        LocalDate.now().plusDays(12)| LocalDate.now().plusDays(13)
        LocalDate.now().plusDays(12)| LocalDate.now().plusDays(14)
        LocalDate.now().plusDays(12)| LocalDate.now().plusDays(15)
        LocalDate.now().plusDays(13)| LocalDate.now().plusDays(14)
        LocalDate.now().plusDays(13)| LocalDate.now().plusDays(15)
        LocalDate.now().plusDays(13)| LocalDate.now().plusDays(16)
        LocalDate.now().plusDays(14)| LocalDate.now().plusDays(15)
        LocalDate.now().plusDays(14)| LocalDate.now().plusDays(16)
        LocalDate.now().plusDays(14)| LocalDate.now().plusDays(17)
        LocalDate.now().plusDays(15)| LocalDate.now().plusDays(16)
        LocalDate.now().plusDays(15)| LocalDate.now().plusDays(17)
        LocalDate.now().plusDays(15)| LocalDate.now().plusDays(18)
        LocalDate.now().plusDays(16)| LocalDate.now().plusDays(17)
        LocalDate.now().plusDays(16)| LocalDate.now().plusDays(18)
        LocalDate.now().plusDays(16)| LocalDate.now().plusDays(19)
        LocalDate.now().plusDays(17)| LocalDate.now().plusDays(18)
        LocalDate.now().plusDays(17)| LocalDate.now().plusDays(19)
        LocalDate.now().plusDays(17)| LocalDate.now().plusDays(20)
        LocalDate.now().plusDays(18)| LocalDate.now().plusDays(19)
        LocalDate.now().plusDays(18)| LocalDate.now().plusDays(20)
        LocalDate.now().plusDays(18)| LocalDate.now().plusDays(21)
        LocalDate.now().plusDays(19)| LocalDate.now().plusDays(20)
        LocalDate.now().plusDays(19)| LocalDate.now().plusDays(21)
        LocalDate.now().plusDays(19)| LocalDate.now().plusDays(22)
        LocalDate.now().plusDays(20)| LocalDate.now().plusDays(21)
        LocalDate.now().plusDays(20)| LocalDate.now().plusDays(22)
        LocalDate.now().plusDays(20)| LocalDate.now().plusDays(23)
        LocalDate.now().plusDays(21)| LocalDate.now().plusDays(22)
        LocalDate.now().plusDays(21)| LocalDate.now().plusDays(23)
        LocalDate.now().plusDays(21)| LocalDate.now().plusDays(24)
        LocalDate.now().plusDays(22)| LocalDate.now().plusDays(23)
        LocalDate.now().plusDays(22)| LocalDate.now().plusDays(24)
        LocalDate.now().plusDays(22)| LocalDate.now().plusDays(25)
        LocalDate.now().plusDays(23)| LocalDate.now().plusDays(24)
        LocalDate.now().plusDays(23)| LocalDate.now().plusDays(25)
        LocalDate.now().plusDays(23)| LocalDate.now().plusDays(26)
        LocalDate.now().plusDays(24)| LocalDate.now().plusDays(25)
        LocalDate.now().plusDays(24)| LocalDate.now().plusDays(26)
        LocalDate.now().plusDays(24)| LocalDate.now().plusDays(27)
        LocalDate.now().plusDays(25)| LocalDate.now().plusDays(26)
        LocalDate.now().plusDays(25)| LocalDate.now().plusDays(27)
        LocalDate.now().plusDays(25)| LocalDate.now().plusDays(28)
        LocalDate.now().plusDays(26)| LocalDate.now().plusDays(27)
        LocalDate.now().plusDays(26)| LocalDate.now().plusDays(28)
        LocalDate.now().plusDays(26)| LocalDate.now().plusDays(29)
        LocalDate.now().plusDays(27)| LocalDate.now().plusDays(28)
        LocalDate.now().plusDays(27)| LocalDate.now().plusDays(29)
        LocalDate.now().plusDays(27)| LocalDate.now().plusDays(30)
        LocalDate.now().plusDays(28)| LocalDate.now().plusDays(29)
        LocalDate.now().plusDays(28)| LocalDate.now().plusDays(30)
        LocalDate.now().plusDays(29)| LocalDate.now().plusDays(30)
    }

    @Unroll
    def "when cancelling a booking"() {
        given:"A cancel reservation request returns status #expectedStatus when the service response to delete is #serviceResponse"
        reservationService.cancelReservation(*_) >> serviceResponse
        when:"When the request is made"
        def response = mockMvc.perform((delete(MsgKeys.CAMPDEMO + MsgKeys.BOOKING_RESOURCE + "1"))).andReturn().getResponse()
        then: "Status is as expected'"
        response.status.equals(expectedStatus)
        where:""
        serviceResponse | expectedStatus
        true            | HttpStatus.ACCEPTED.value()
        false           | HttpStatus.EXPECTATION_FAILED.value()
    }
}
