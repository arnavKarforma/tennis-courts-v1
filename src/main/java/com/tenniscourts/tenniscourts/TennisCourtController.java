package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TennisCourtController extends BaseRestController {

    private final TennisCourtServiceImpl tennisCourtService;

    @Autowired
    public TennisCourtController(final TennisCourtServiceImpl tennisCourtService) {
        this.tennisCourtService = tennisCourtService;
    }

    @RequestMapping(value = "/tennisCourt", method = RequestMethod.POST)
    public ResponseEntity<Void> addTennisCourt(@RequestBody final TennisCourtDTO tennisCourtDTO) {
        return ResponseEntity.created(locationByEntity(
                this.tennisCourtService.addTennisCourt(tennisCourtDTO).getId())).build();
    }

    @RequestMapping(value = "/tennisCourt/{id}", method = RequestMethod.GET)
    public ResponseEntity<TennisCourtDTO> findTennisCourtById(@PathVariable(value = "id") final Long tennisCourtId) {
        return ResponseEntity.ok(this.tennisCourtService.findTennisCourtById(tennisCourtId));
    }

    @RequestMapping(value = "/tennisCourt/list-schedule/{id}", method = RequestMethod.GET)
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@PathVariable(value = "id") final Long tennisCourtId) {
        return ResponseEntity.ok(this.tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
    }


    @RequestMapping(value = "/tennisCourt")
    public ResponseEntity<List<TennisCourtDTO>> listAllTennisCourts() {
        return ResponseEntity.ok(this.tennisCourtService.listAllTennisCourts());
    }
}
