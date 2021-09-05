package com.tenniscourts.guest;

import com.tenniscourts.TestDataUtilities;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private GuestServiceImpl guestService;

    @Test
    public void createGuestSuccess() {
        final Guest guestInDb = TestDataUtilities.getTestGuest();
        final Guest guestToBeSaved = TestDataUtilities.getTestGuest();
        guestToBeSaved.setId(null);
        when(this.guestRepository.save(guestToBeSaved)).thenReturn(guestInDb);
        final GuestResponse guestDTO = this.guestService.createGuest(TestDataUtilities.getTestGuestRequestDTO());
        Assert.assertEquals(guestDTO.getId(), guestInDb.getId());
        Assert.assertEquals(guestDTO.getName(), guestInDb.getName());
        guestInDb.setId(null);
        verify(this.guestRepository).save(guestInDb);
    }

    @Test
    public void updateGuestSuccess() {
        final Guest guestInDb = TestDataUtilities.getTestGuest();
        final GuestRequestDTO updateRequest = TestDataUtilities.getTestGuestRequestDTOSecond();
        when(this.guestRepository.findById(any())).thenReturn(Optional.of(guestInDb));
        guestInDb.setName(updateRequest.getName());
        when(this.guestRepository.save(any())).thenReturn(guestInDb);
        final GuestResponse guestDTO = this.guestService.updateGuest(guestInDb.getId(), updateRequest);
        assertEquals(guestDTO.getId(), guestInDb.getId());
        assertEquals(guestDTO.getName(), updateRequest.getName());
        verify(this.guestRepository).findById(1L);
        verify(this.guestRepository).save(guestInDb);
    }

    @Test
    public void updateGuestFailureGuestNotPresent() {
        final Guest guestInDb = TestDataUtilities.getTestGuest();
        final GuestRequestDTO updateRequest = TestDataUtilities.getTestGuestRequestDTOSecond();
        when(this.guestRepository.findById(any())).thenReturn(Optional.empty());

        final Throwable throwable = catchThrowable(() -> this.guestService.updateGuest(guestInDb.getId(), updateRequest));
        assertNotEquals(null, throwable);
        assertTrue(throwable instanceof EntityNotFoundException);
        verify(this.guestRepository).findById(1L);
    }

    @Test
    public void deleteGuestSuccess() {
        doNothing().when(this.guestRepository).deleteById(any());
        this.guestService.deleteGuest(1L);
        verify(this.guestRepository).deleteById(1L);
    }

    @Test
    public void getGuestsByNameSuccess() {
        final String name = "Arnav";
        final List<Guest> guests = List.of(TestDataUtilities.getTestGuest());
        when(this.guestRepository.findByNameContains(name)).thenReturn(Optional.of(guests));
        final List<GuestResponse> guestDTO = this.guestService.getGuestsByName(name);
        assertEquals(1, guestDTO.size());
        assertEquals(guests.get(0).getName(), guestDTO.get(0).getName());
        assertEquals(1, guestDTO.get(0).getId());
        verify(this.guestRepository).findByNameContains(name);
    }

    @Test
    public void getGuestsByNameFailureEntityNotFound() {
        final String name = "Arnav";
        when(this.guestRepository.findByNameContains(name)).thenReturn(Optional.empty());
        final Throwable throwable = catchThrowable(() -> this.guestService.getGuestsByName(name));
        assertNotEquals(null, throwable);
        assertTrue(throwable instanceof EntityNotFoundException);
        verify(this.guestRepository).findByNameContains(name);
    }

    @Test
    public void getGuestsByIdSuccess() {
        final Guest guest = TestDataUtilities.getTestGuest();
        when(this.guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        final GuestResponse guestDTO = this.guestService.getGuestById(1L);
        assertEquals(guest.getName(), guestDTO.getName());
        assertEquals(guest.getId(), guestDTO.getId());
        verify(this.guestRepository).findById(1L);
    }

    @Test
    public void getGuestsByIdFailureWhenGuestIsNotFound() {
        when(this.guestRepository.findById(1L)).thenReturn(Optional.empty());
        final Throwable throwable = catchThrowable(() -> this.guestService.getGuestById(1L));
        assertNotEquals(null, throwable);
        assertTrue(throwable instanceof EntityNotFoundException);
        verify(this.guestRepository).findById(1L);
    }

    @Test
    public void findAllSuccess() {
        final List<Guest> guests = List.of(TestDataUtilities.getTestGuest());
        when(this.guestRepository.findAll()).thenReturn(guests);
        final List<GuestResponse> guestDTO = this.guestService.findAll();
        assertEquals(1, guestDTO.size());
        assertEquals(guests.get(0).getName(), guestDTO.get(0).getName());
        assertEquals(1, guestDTO.get(0).getId());
        verify(this.guestRepository).findAll();
    }
}