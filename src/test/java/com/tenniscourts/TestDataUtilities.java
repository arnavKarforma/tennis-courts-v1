package com.tenniscourts;

import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestRequestDTO;
import com.tenniscourts.guests.GuestResponse;

import java.util.ArrayList;
import java.util.List;

public class TestDataUtilities {
    public static Guest getTestGuest() {
        final Guest guest = Guest.builder().name("Arnav Karforma").build();
        guest.setId(1L);
        return guest;
    }

    public static List<Guest> getTestGuests() {
        final List<Guest> guestList = new ArrayList<>();
        guestList.add(getTestGuest());
        guestList.add(getTestGuest());
        return guestList;
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
}
