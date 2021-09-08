package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.schedules.ScheduleRepository;
import com.tenniscourts.schedules.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.tenniscourts.reservations.ReservationStatus.READY_TO_PLAY;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    private final ScheduleRepository scheduleRepository;

    private final GuestRepository guestRepository;

    @Autowired
    public ReservationServiceImpl(final ReservationRepository reservationRepository,
                                  final ReservationMapper reservationMapper,
                                  final ScheduleRepository scheduleRepository,
                                  final GuestRepository guestRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.scheduleRepository = scheduleRepository;
        this.guestRepository = guestRepository;
    }


    @Override
    @Transactional
    public ReservationDTO bookReservation(final CreateReservationRequestDTO createReservationRequestDTO) {
        final Schedule schedule = this.scheduleRepository.findById(createReservationRequestDTO.getScheduleId())
                .orElseThrow(()-> new EntityNotFoundException("Schedule not found to book reservation"));
        final Guest guest = this.guestRepository.findById(createReservationRequestDTO.getGuestId())
                .orElseThrow(()-> new EntityNotFoundException("Schedule not found to book reservation"));
        final Reservation reservation = this.reservationMapper.map(createReservationRequestDTO);
        reservation.setSchedule(schedule);
        reservation.setReservationStatus(READY_TO_PLAY);
        reservation.setGuest(guest);
        reservation.setValue(BigDecimal.TEN);
        reservation.setRefundValue(getRefundValue(reservation));
        this.reservationRepository.save(reservation);
        return this.reservationMapper.map(reservation);
    }

    @Override
    public ReservationDTO findReservation(final Long reservationId) {
        return this.reservationRepository.findById(reservationId)
                .map(this.reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    @Override
    public ReservationDTO cancelReservation(final Long reservationId) {
        final Reservation cancelledReservation = this.reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellationOrReschedule(reservation);

            final BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
        return this.reservationMapper.map(cancelledReservation);
    }

    @Override
    public Reservation updateReservation(final Reservation reservation, final BigDecimal refundValue,
                                         final ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return this.reservationRepository.save(reservation);
    }

    @Override
    public void validateCancellationOrReschedule(final Reservation reservation) {
        if (!READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    @Override
    public BigDecimal getRefundValue(final Reservation reservation) {
        final long minutes = ChronoUnit.MINUTES.between(
                LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (minutes > 0 && minutes < 2 * 60) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.25));
        }
        else if (minutes >= 2 * 60 && minutes < 12 * 60) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.5));
        }
        else if (minutes >= 12 * 60 && minutes < 24 * 60) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.75));
        }
        else if (minutes >= 24 * 60) {
            return reservation.getValue();
        }
        return BigDecimal.ZERO;
    }


    /**
     * Step 1: Check if the reservationId still exists in db and is in READY_TO_PLAY status
     * Step 2: Check if the scheduleId is till present in the db
     * Step 3: Check if the user is requesting to schedule to the same timeslot again in the same tennis court, if yes reject the request
     * Step 4: Validate if the reservation still can be modified
     * Step 5: If request has reached till here, update the reservation as Rescheduled
     * Step 6: Book a new reservation, save the old one in response DTO
     *
     * @param previousReservationId
     * @param scheduleId
     * @return
     */
    @Override
    @Transactional
    public ReservationDTO rescheduleReservation(final Long previousReservationId, final Long scheduleId) {
        Reservation previousReservation =  this.reservationRepository.findById(previousReservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found."));

        if(!READY_TO_PLAY.equals(previousReservation.getReservationStatus())){
            throw new IllegalArgumentException("Reservations with status Ready to play can only be rescheduled");
        }
        final Schedule newSchedule = this.scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("New Schedule not found."));

        if (newSchedule.getStartDateTime().equals(previousReservation.getSchedule().getStartDateTime())
                && newSchedule.getTennisCourt().getId().equals(previousReservation.getSchedule().getTennisCourt().getId())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        this.validateCancellationOrReschedule(previousReservation);

        final BigDecimal refundValue = getRefundValue(previousReservation);

        previousReservation = this.updateReservation(previousReservation, refundValue, ReservationStatus.RESCHEDULED);

        final ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(this.reservationMapper.map(previousReservation));
        return newReservation;
    }
}
