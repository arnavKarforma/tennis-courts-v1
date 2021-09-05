package com.tenniscourts.guests;


import org.springframework.stereotype.Service;

import java.util.List;

public interface GuestService {

    GuestResponse createGuest(GuestRequestDTO guestRequestDTO);

    GuestResponse updateGuest(Long guestId, GuestRequestDTO guestRequestDTO);

    void deleteGuest(Long guestId);

    List<GuestResponse> getGuestsByName(String name);

    GuestResponse getGuestById(Long guestId);

    List<GuestResponse> findAll();
}
