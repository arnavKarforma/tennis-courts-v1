package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
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

    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody final CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(
                this.scheduleService.addSchedule(
                        createScheduleRequestDTO.getTennisCourtId(),
                        createScheduleRequestDTO).getId())).build();
    }

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

    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable(value = "id") final Long scheduleId) {
        return ResponseEntity.ok(this.scheduleService.findSchedule(scheduleId));
    }
}
