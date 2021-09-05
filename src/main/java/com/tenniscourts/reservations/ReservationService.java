package com.tenniscourts.reservations;

import java.math.BigDecimal;

public interface ReservationService {
    ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO);

    ReservationDTO findReservation(Long reservationId);

    ReservationDTO cancelReservation(Long reservationId);

    Reservation cancel(Long reservationId);

    Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status);

    void validateCancellationOrReschedule(Reservation reservation);

    BigDecimal getRefundValue(Reservation reservation);

    /*TODO: This method actually not fully working, find a way to fix the issue when it's throwing the error:
                "Cannot reschedule to the same slot.*/
    ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId);
}
