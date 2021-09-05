package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @Autowired
    public GuestController(final GuestService guestService) {
        this.guestService = guestService;
    }

    @RequestMapping(path = "/guest", method = RequestMethod.POST)
    public ResponseEntity<GuestResponse> createGuest(@RequestBody final GuestRequestDTO guestRequestDTO) {
        return ResponseEntity.created(
                        locationByEntity(
                                this.guestService
                                        .createGuest(guestRequestDTO).getId()))
                .build();
    }

    @RequestMapping(path = "/guest", method = RequestMethod.GET)
    public ResponseEntity<List<GuestResponse>> findAllGuest() {
        return ResponseEntity.ok(this.guestService.findAll());
    }

    @RequestMapping(path = "/guest/{id}", method = RequestMethod.PUT)
    public ResponseEntity<GuestResponse> updateGuest(@PathVariable("id") final Long id,
                                     @RequestBody final GuestRequestDTO updateGuestDTO) {
        return ResponseEntity.ok(this.guestService.updateGuest(id, updateGuestDTO));
    }

    @RequestMapping(path = "/guest/{id}", method = RequestMethod.DELETE)
    public void deleteGuest(@PathVariable("id") final Long id) {
        this.guestService.deleteGuest(id);
    }

    @RequestMapping(path = "/guest/{id}", method = RequestMethod.GET)
    public ResponseEntity<GuestResponse> findGuestById(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(this.guestService.getGuestById(id));
    }

    @RequestMapping(path = "/guest/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<GuestResponse>> findGuestsByName(@PathVariable("name") final String name) {
        return ResponseEntity.ok(this.guestService.getGuestsByName(name));
    }
}