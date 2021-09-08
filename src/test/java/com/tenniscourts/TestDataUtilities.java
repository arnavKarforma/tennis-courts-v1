package com.tenniscourts;

import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestRequestDTO;
import com.tenniscourts.guests.GuestResponse;
import com.tenniscourts.reservations.CreateReservationRequestDTO;
import com.tenniscourts.reservations.Reservation;
import com.tenniscourts.schedules.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtDTO;

import java.time.LocalDateTime;

public class TestDataUtilities {
    public static Guest getTestGuest() {
        final Guest guest = Guest.builder().name("Arnav Karforma").build();
        guest.setId(1L);
        return guest;
    }


    public static GuestResponse getTestGuestResponse() {
        return GuestResponse.builder()
                .id(1L)
                .name("Arnav Karforma")
                .build();
    }

    public static GuestRequestDTO getTestGuestRequestDTO() {
        return GuestRequestDTO.builder()
                .name("Arnav Karforma")
                .build();
    }

    public static GuestRequestDTO getTestGuestRequestDTOSecond() {
        return GuestRequestDTO.builder()
                .name("Arnav New")
                .build();
    }

    public static TennisCourt getTestTennisCourt() {
        final TennisCourt tennisCourt = new TennisCourt();
        tennisCourt.setName("Arnav Tennis Court");
        tennisCourt.setId(1L);
        return tennisCourt;
    }

    public static TennisCourtDTO getTestTennisCourtDTO() {
        return TennisCourtDTO.builder()
                .name("Arnav Tennis Court")
                .build();
    }

    public static TennisCourtDTO getTestTennisCourtResponseDTO() {
        return TennisCourtDTO.builder()
                .name("Arnav Tennis Court")
                .id(1L)
                .build();
    }

    public static Schedule getTestSchedule() {
        final Schedule schedule = Schedule.builder()
                .tennisCourt(getTestTennisCourt())
                .startDateTime(LocalDateTime.now().plusDays(2))
                .endDateTime(LocalDateTime.now().plusDays(3))
                .build();
        schedule.setId(1L);
        return schedule;
    }

    public static CreateScheduleRequestDTO getTestScheduleDTO() {
        return CreateScheduleRequestDTO.builder()
                .startDateTime(LocalDateTime.now().plusDays(2))
                .build();
    }

    public static CreateReservationRequestDTO getTestReservationRequestDTO() {
        return CreateReservationRequestDTO.builder()
                .guestId(1L)
                .scheduleId(1L)
                .build();
    }


    public static ScheduleDTO getTestScheduleResponseDTO() {
        final ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .tennisCourt(getTestTennisCourtResponseDTO())
                .startDateTime(LocalDateTime.now().plusDays(2))
                .endDateTime(LocalDateTime.now().plusDays(3))
                .build();
        scheduleDTO.setId(1L);
        return scheduleDTO;
    }
}
