package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Controller
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @ApiOperation(value = "It will create a new Reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Reservation created"),
            @ApiResponse(code = 404, message = "Guest or Schedule not found")
    })
    @RequestMapping(path = "/reservation", method = RequestMethod.POST)
    public ResponseEntity<Void> bookReservation(@RequestBody final CreateReservationRequestDTO createReservationRequestDTO) throws Exception {
        return ResponseEntity.created(locationByEntity(this.reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @ApiOperation(value = "It will find a existing Reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservation searched"),
            @ApiResponse(code = 404, message = "No reservation found")
    })
    @RequestMapping(path = "/reservation/{reservationId}", method = RequestMethod.GET)
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable("reservationId") final Long reservationId) {
        return ResponseEntity.ok(this.reservationService.findReservation(reservationId));
    }

    @ApiOperation(value = "It will cancel a existing Reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservation cancelled"),
            @ApiResponse(code = 404, message = "No reservation found"),
            @ApiResponse(code = 400, message = "Reservation not in correct status or startTime")
    })
    @RequestMapping(path = "/reservation/cancel/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable("id") final Long reservationId) {
        return ResponseEntity.ok(this.reservationService.cancelReservation(reservationId));
    }


    @ApiOperation(value = "It will reschedule a existing Reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservation rescheduled"),
            @ApiResponse(code = 404, message = "No reservation or new schedule not found"),
            @ApiResponse(code = 400, message = "Reservation not in correct status or new schedule is not on valid startTime")
    })
    @RequestMapping(path = "/reservation/reschedule/{reservationId}/{scheduleId}", method = RequestMethod.PUT)
    public ResponseEntity<ReservationDTO> rescheduleReservation(@PathVariable("reservationId") final Long reservationId, @PathVariable("scheduleId") final Long scheduleId) throws Exception {
        return ResponseEntity.ok(this.reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
