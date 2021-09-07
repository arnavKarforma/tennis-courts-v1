package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
public class ScheduleController extends BaseRestController {

    private final ScheduleServiceImpl scheduleService;

    @Autowired
    public ScheduleController(final ScheduleServiceImpl scheduleService) {
        this.scheduleService = scheduleService;
    }

    @ApiOperation(value = "It will create a new Schedule")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Schedule created"),
            @ApiResponse(code = 404, message = "Tennis Court not found")
    })
    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody final CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(
                this.scheduleService.addSchedule(
                        createScheduleRequestDTO.getTennisCourtId(),
                        createScheduleRequestDTO).getId())).build();
    }

    @ApiOperation(value = "It will create a new Schedule")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Schedule searched"),
            @ApiResponse(code = 404, message = "Schedules not found")
    })
    @RequestMapping(value = "/schedule/availability", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final
                                                                  LocalDate startDate,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final
                                                                  LocalDate endDate) {
        return ResponseEntity.ok(
                this.scheduleService.findSchedulesByDates(
                        LocalDateTime.of(startDate, LocalTime.of(0, 0)),
                        LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @ApiOperation(value = "It will create a new Schedule")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Schedule searched"),
            @ApiResponse(code = 404, message = "Schedules not found")
    })
    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable(value = "id") final Long scheduleId) {
        return ResponseEntity.ok(this.scheduleService.findSchedule(scheduleId));
    }
}
