package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    @Autowired
    public GuestServiceImpl(final GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }


    @Override
    public GuestResponse createGuest(final GuestRequestDTO guestRequestDTO) {
        return GuestMapper.toGuestResponse(
                this.guestRepository.save(GuestMapper.toGuest(guestRequestDTO)));
    }

    @Override
    @Transactional
    public GuestResponse updateGuest(final Long guestId, final GuestRequestDTO guestRequestDTO) {
        final Guest guest = this.guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));
        guest.setName(guestRequestDTO.getName());
        return GuestMapper.toGuestResponse(this.guestRepository.save(guest));
    }

    @Override
    public void deleteGuest(final Long guestId) {
        this.guestRepository.deleteById(guestId);
    }

    @Override
    public List<GuestResponse> getGuestsByName(final String name) {
        final List<Guest> guests = this.guestRepository.findByNameContains(name)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));
        return guests.stream().map(GuestMapper::toGuestResponse).collect(Collectors.toList());
    }

    @Override
    public GuestResponse getGuestById(final Long guestId) {
        final Guest guest = this.guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));
        return GuestMapper.toGuestResponse(guest);
    }

    @Override
    public List<GuestResponse> findAll() {
        return this.guestRepository.findAll().stream()
                .map(GuestMapper::toGuestResponse).collect(Collectors.toList());
    }
}
