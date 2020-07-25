package com.jcampos.campdemo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.jcampos.campdemo.model.dto.BookingDto
import com.jcampos.campdemo.services.ReservationService
import com.jcampos.campdemo.util.MsgKeys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate
import java.util.stream.IntStream

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@ActiveProfiles(profiles = "test")
@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerSpecIT extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    @Autowired
    private ReservationService reservationService

    @Unroll
    def "Test updating a booked reservation"() {
        given: "Create 2 bookings"
        def saveArrivalDate = LocalDate.now().plusDays(3)
        def saveDepartureDate = LocalDate.now().plusDays(4)
        def firstBookingDto = new BookingDto(
                firstName: "John",
                lastName: "Doe",
                email: "jdoe@email.com",
                arrivalDate: saveArrivalDate,
                departureDate: saveDepartureDate
        )
        def firstJsonContent = new ObjectMapper().writeValueAsString(firstBookingDto)
        def secondBookingDto = new BookingDto(
                firstName: "Jane",
                lastName: "Doe",
                email: "jdoe@email.com",
                arrivalDate: saveArrivalDate.plusDays(10),
                departureDate: saveDepartureDate.plusDays(10)
        )
        def secondJsonContent = new ObjectMapper().writeValueAsString(secondBookingDto)
        when: "Save the first two bookings 10 days apart"
        def firstResponse = mockMvc.perform((post(MsgKeys.CAMPDEMO + MsgKeys.BOOKING_RESOURCE)).contentType(MediaType.APPLICATION_JSON).content(firstJsonContent)).andReturn().getResponse()
        def secondResponse = mockMvc.perform((post(MsgKeys.CAMPDEMO + MsgKeys.BOOKING_RESOURCE)).contentType(MediaType.APPLICATION_JSON).content(secondJsonContent)).andReturn().getResponse()
        and: "Updating first reservation 8 times. The 4th time the reservation update will run into having the same dates as second reservation"
        firstBookingDto.setId(Long.parseLong(firstResponse.contentAsString))
        List<MockHttpServletResponse> updateResponses = new ArrayList<>()
        for (int i = 1; i < 8; i++) {
            firstBookingDto.setArrivalDate(firstBookingDto.getArrivalDate().plusDays(i))
            firstBookingDto.setDepartureDate(firstBookingDto.getDepartureDate().plusDays(i))
            firstJsonContent = new ObjectMapper().writeValueAsString(firstBookingDto)
            def updateResponse = mockMvc.perform((put(MsgKeys.CAMPDEMO + MsgKeys.BOOKING_RESOURCE + "/1")).contentType(MediaType.APPLICATION_JSON).content(firstJsonContent)).andReturn().getResponse()
            updateResponses.add(updateResponse)
        }
        then: "Check the reservations were created properly"
        firstResponse.getStatus() == HttpStatus.CREATED.value()
        secondResponse.getStatus() == HttpStatus.CREATED.value()
        and: "Check that 7 updates were done, the 4th update was rejected due to dates being unavailable"
        updateResponses.size() == 7
        int i = 1;
        for (MockHttpServletResponse update : updateResponses) {
            switch (i) {
                case 4:
                    assert update.status == HttpStatus.BAD_REQUEST.value()
                    assert update.contentAsString.contains("Dates are not available")
                    break
                case 7:
                    assert update.status == HttpStatus.BAD_REQUEST.value()
                    assert update.contentAsString.contains("Validation failed for argument")
                    break
                default:
                    assert update.status == HttpStatus.OK.value()

            }
            i++
        }
    }

    @Unroll
    def "Test creating a  reservation when there is one in the system already"() {
        given: "Create 2 bookings"
        def saveArrivalDate = LocalDate.now().plusDays(15)
        def saveDepartureDate = LocalDate.now().plusDays(16)
        def firstBookingDto = new BookingDto(
                firstName: "Michael",
                lastName: "Smith",
                email: "msmith@email.com",
                arrivalDate: saveArrivalDate,
                departureDate: saveDepartureDate
        )
        def firstJsonContent = new ObjectMapper().writeValueAsString(firstBookingDto)
        def secondBookingDto = new BookingDto(
                firstName: "Michael",
                lastName: "Smith",
                email: "msmith@email.com",
                arrivalDate: saveArrivalDate.plusDays(1),
                departureDate: saveDepartureDate.plusDays(1)
        )
        def secondJsonContent = new ObjectMapper().writeValueAsString(secondBookingDto)
        when: "Save the first two bookings 10 days apart"
        def firstResponse = mockMvc.perform((post(MsgKeys.CAMPDEMO + MsgKeys.BOOKING_RESOURCE)).contentType(MediaType.APPLICATION_JSON).content(firstJsonContent)).andReturn().getResponse()
        def secondResponse = mockMvc.perform((post(MsgKeys.CAMPDEMO + MsgKeys.BOOKING_RESOURCE)).contentType(MediaType.APPLICATION_JSON).content(secondJsonContent)).andReturn().getResponse()
        then: "The second response return a guest already exists message"
        firstResponse.status == HttpStatus.CREATED.value()
        secondResponse.status == HttpStatus.BAD_REQUEST.value()
        secondResponse.contentAsString.contains("Guest already has booking")
    }


}