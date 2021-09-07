package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;

    private final TennisCourtRepository tennisCourtRepository;

    @Autowired
    public ScheduleServiceImpl(final ScheduleRepository scheduleRepository, final ScheduleMapper scheduleMapper,
                               final TennisCourtRepository tennisCourtRepository) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleMapper = scheduleMapper;
        this.tennisCourtRepository = tennisCourtRepository;
    }

    @Override
    @Transactional
    public ScheduleDTO addSchedule(final Long tennisCourtId, final CreateScheduleRequestDTO createScheduleRequestDTO) {
        final TennisCourt court = this.tennisCourtRepository.findById(tennisCourtId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Tennis Court not found");
                });
        Schedule schedule = Schedule.builder()
                .tennisCourt(court)
                .startDateTime(createScheduleRequestDTO.getStartDateTime())
                .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1L))
                .build();
        schedule = this.scheduleRepository.save(schedule);
        return this.scheduleMapper.map(schedule);
    }

    @Override
    public List<ScheduleDTO> findSchedulesByDates(final LocalDateTime startDate, final LocalDateTime endDate) {
        final List<Schedule> schedules = this.scheduleRepository.findSchedules(startDate, endDate);
        if (schedules.isEmpty()) {
            throw new EntityNotFoundException("Schedule not found.");
        }
        return this.scheduleMapper.map(schedules);
    }

    @Override
    public ScheduleDTO findSchedule(final Long scheduleId) {
        return this.scheduleMapper.map(this.scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Schedule not found.");
                }));
    }

    @Override
    public List<ScheduleDTO> findSchedulesByTennisCourtId(final Long tennisCourtId) {
        return this.scheduleMapper.map(this.scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
