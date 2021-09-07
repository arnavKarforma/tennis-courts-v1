package com.tenniscourts.reservations;

import com.tenniscourts.TestDataUtilities;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleRepository;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.tenniscourts.reservations.ReservationStatus.READY_TO_PLAY;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationServiceImpl.class)
public class ReservationServiceTest {

    @Spy
    @InjectMocks
    private ReservationServiceImpl reservationServiceImpl;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private ScheduleRepository scheduleRepository;


    @Test
    public void getRefundValueFullRefund() {
        final Schedule schedule = new Schedule();

        final LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(new BigDecimal(10), this.reservationServiceImpl.getRefundValue(
                Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));

        schedule.setStartDateTime(LocalDateTime.now().plusHours(1));
        Assert.assertEquals(new BigDecimal("2.50"), this.reservationServiceImpl.getRefundValue(
                Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));

        schedule.setStartDateTime(LocalDateTime.now().plusHours(3));
        Assert.assertEquals(new BigDecimal("5.0"), this.reservationServiceImpl.getRefundValue(
                Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));

        schedule.setStartDateTime(LocalDateTime.now().plusHours(12));
        Assert.assertEquals(new BigDecimal("7.50"), this.reservationServiceImpl.getRefundValue(
                Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));
    }

    @Test
    public void validateCancellationOrRescheduleSuccess() {
        final Schedule schedule = new Schedule();

        final Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.CANCELLED);

        Assert.assertTrue(catchThrowable(() -> this.reservationServiceImpl.validateCancellationOrReschedule(reservation))
                instanceof IllegalArgumentException);

        reservation.setReservationStatus(ReservationStatus.READY_TO_PLAY);
        final LocalDateTime startDateTime = LocalDateTime.now().minusDays(2);
        schedule.setStartDateTime(startDateTime);
        reservation.setSchedule(schedule);
        Assert.assertTrue(catchThrowable(() -> this.reservationServiceImpl.validateCancellationOrReschedule(reservation))
                instanceof IllegalArgumentException);

    }

    @Test
    public void rescheduleReservationFailureReservationNotFound() {
        when(this.reservationRepository.findById(1L)).thenReturn(Optional.empty());
        Assert.assertTrue(catchThrowable(() -> this.reservationServiceImpl.rescheduleReservation(1L, 1L))
                instanceof EntityNotFoundException);

    }

    @Test
    public void rescheduleReservationFailureReservationNotReadyToPlay() {
        final Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        when(this.reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        Assert.assertTrue(catchThrowable(() -> this.reservationServiceImpl.rescheduleReservation(1L, 1L))
                instanceof IllegalArgumentException);
        verify(this.reservationRepository).findById(1L);

    }

    @Test
    public void rescheduleReservationFailureScheduleNotFound() {
        final Reservation reservation = new Reservation();
        reservation.setReservationStatus(READY_TO_PLAY);
        when(this.reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(this.scheduleRepository.findById(1L)).thenReturn(Optional.empty());
        Assert.assertTrue(catchThrowable(() -> this.reservationServiceImpl.rescheduleReservation(1L, 1L))
                instanceof EntityNotFoundException);
        verify(this.reservationRepository).findById(1L);

    }

    @Test
    public void rescheduleReservationFailureStartDateTimeMatches() {
        final LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);
        final Reservation reservation = new Reservation();
        reservation.setReservationStatus(READY_TO_PLAY);
        final Schedule schedule = new Schedule();
        schedule.setStartDateTime(startDateTime);
        reservation.setSchedule(schedule);
        when(this.reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(this.scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        Assert.assertTrue(catchThrowable(() -> this.reservationServiceImpl.rescheduleReservation(1L, 1L))
                instanceof IllegalArgumentException);
        verify(this.reservationRepository).findById(1L);
        verify(this.scheduleRepository).findById(1L);
    }

    @Test
    public void rescheduleReservationSuccess() {
        final LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);
        final Reservation reservation = new Reservation();
        reservation.setReservationStatus(READY_TO_PLAY);
        final Schedule scheduleInternal = new Schedule();
        scheduleInternal.setStartDateTime(startDateTime);
        reservation.setSchedule(scheduleInternal);
        reservation.setGuest(TestDataUtilities.getTestGuest());
        final Schedule schedule = new Schedule();
        schedule.setStartDateTime(LocalDateTime.now().plusDays(3));
        final ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setReservationStatus(ReservationStatus.RESCHEDULED.toString());
        when(this.reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(this.scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        doNothing().when(this.reservationServiceImpl).validateCancellationOrReschedule(reservation);
        doReturn(BigDecimal.valueOf(7)).when(this.reservationServiceImpl).getRefundValue(reservation);
        doReturn(reservation).when(this.reservationServiceImpl).updateReservation(reservation, BigDecimal.valueOf(7), ReservationStatus.RESCHEDULED);
        doReturn(new ReservationDTO()).when(this.reservationServiceImpl).bookReservation(any(CreateReservationRequestDTO.class));
        when(this.reservationMapper.map(reservation)).thenReturn(reservationDTO);

        final ReservationDTO reservationDTORes = this.reservationServiceImpl.rescheduleReservation(1L, 1L);

        assertEquals(reservationDTO, reservationDTORes.getPreviousReservation());

        //Todo: Add assert check for values
        verify(this.reservationRepository).findById(1L);
        verify(this.scheduleRepository).findById(1L);
        verify(this.reservationServiceImpl).validateCancellationOrReschedule(reservation);
        verify(this.reservationServiceImpl).getRefundValue(reservation);
        verify(this.reservationServiceImpl).updateReservation(reservation, BigDecimal.valueOf(7), ReservationStatus.RESCHEDULED);
        verify(this.reservationServiceImpl).bookReservation(any(CreateReservationRequestDTO.class));
    }
}