package com.tenniscourts.reservations;

import java.math.BigDecimal;

public interface ReservationService {
    ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO);

    ReservationDTO findReservation(Long reservationId);

    ReservationDTO cancelReservation(Long reservationId);

    Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status);

    void validateCancellationOrReschedule(Reservation reservation);

    BigDecimal getRefundValue(Reservation reservation);

    ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId);
}
