package com.tenniscourts.guests;

public final class GuestMapper {

    static GuestResponse toGuestResponse(final Guest guest) {
        return GuestResponse.builder()
                .id(guest.getId())
                .name(guest.getName())
                .build();
    }

    static Guest toGuest(final GuestRequestDTO guestRequestDTO) {
        return Guest.builder()
                .name(guestRequestDTO.getName())
                .build();
    }
}
