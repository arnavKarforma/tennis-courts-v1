package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
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

    @RequestMapping(path = "/reservation", method = RequestMethod.POST)
    public ResponseEntity<Void> bookReservation(@RequestBody final CreateReservationRequestDTO createReservationRequestDTO) throws Exception {
        return ResponseEntity.created(locationByEntity(this.reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @RequestMapping(path = "/reservation/{reservationId}", method = RequestMethod.GET)
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable("reservationId") final Long reservationId) {
        return ResponseEntity.ok(this.reservationService.findReservation(reservationId));
    }

    @RequestMapping(path = "/reservation/cancel/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable("id") final Long reservationId) {
        return ResponseEntity.ok(this.reservationService.cancelReservation(reservationId));
    }

    @RequestMapping(path = "/reservation/reschedule/{reservationId}/{scheduleId}", method = RequestMethod.PUT)
    public ResponseEntity<ReservationDTO> rescheduleReservation(@PathVariable("reservationId") final Long reservationId, @PathVariable("scheduleId") final Long scheduleId) throws Exception {
        return ResponseEntity.ok(this.reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
