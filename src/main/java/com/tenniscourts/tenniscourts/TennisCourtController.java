package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "It will create a new tennis court entry")
    @ApiResponse(code = 201, message = "Tennis Court Created")
    @RequestMapping(value = "/tennisCourt", method = RequestMethod.POST)
    public ResponseEntity<Void> addTennisCourt(@RequestBody final TennisCourtDTO tennisCourtDTO) {
        return ResponseEntity.created(locationByEntity(
                this.tennisCourtService.addTennisCourt(tennisCourtDTO).getId())).build();
    }

    @ApiOperation(value = "Find tennis Court By Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tennis Court searched"),
            @ApiResponse(code = 404, message = "Tennis Court not found")
    })
    @RequestMapping(value = "/tennisCourt/{id}", method = RequestMethod.GET)
    public ResponseEntity<TennisCourtDTO> findTennisCourtById(@PathVariable(value = "id") final Long tennisCourtId) {
        return ResponseEntity.ok(this.tennisCourtService.findTennisCourtById(tennisCourtId));
    }

    @ApiOperation(value = "Find tennis Court By Id with schedules details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tennis Court searched"),
            @ApiResponse(code = 404, message = "Tennis Court not found")
    })
    @RequestMapping(value = "/tennisCourt/list-schedule/{id}", method = RequestMethod.GET)
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@PathVariable(value = "id") final Long tennisCourtId) {
        return ResponseEntity.ok(this.tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
    }

    @ApiOperation(value = "Find all Tennis Courts")
    @ApiResponse(code = 200, message = "Search Done")
    @RequestMapping(value = "/tennisCourt", method = RequestMethod.GET)
    public ResponseEntity<List<TennisCourtDTO>> listAllTennisCourts() {
        return ResponseEntity.ok(this.tennisCourtService.listAllTennisCourts());
    }
}
