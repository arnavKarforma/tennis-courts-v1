package com.tenniscourts.tenniscourts;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.ScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TennisCourtServiceImpl implements TennisCourtService {

    private final TennisCourtRepository tennisCourtRepository;

    private final ScheduleServiceImpl scheduleService;

    private final TennisCourtMapper tennisCourtMapper;

    @Autowired
    public TennisCourtServiceImpl(final TennisCourtRepository tennisCourtRepository, final ScheduleServiceImpl scheduleService,
                                  final TennisCourtMapper tennisCourtMapper) {
        this.tennisCourtRepository = tennisCourtRepository;
        this.scheduleService = scheduleService;
        this.tennisCourtMapper = tennisCourtMapper;
    }

    @Override
    public TennisCourtDTO addTennisCourt(final TennisCourtDTO tennisCourt) {
        return this.tennisCourtMapper.map(
                this.tennisCourtRepository.save(this.tennisCourtMapper.map(tennisCourt)));
    }

    @Override
    public TennisCourtDTO findTennisCourtById(final Long id) {
        return this.tennisCourtRepository.findById(id).map(this.tennisCourtMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Tennis Court not found.");
        });
    }

    @Override
    public TennisCourtDTO findTennisCourtWithSchedulesById(final Long tennisCourtId) {
        final TennisCourtDTO tennisCourtDTO = findTennisCourtById(tennisCourtId);
        tennisCourtDTO.setTennisCourtSchedules(this.scheduleService.findSchedulesByTennisCourtId(tennisCourtId));
        return tennisCourtDTO;
    }

    @Override
    public List<TennisCourtDTO> listAllTennisCourts() {
        return this.tennisCourtRepository.findAll().stream()
                .map(this.tennisCourtMapper::map)
                .collect(Collectors.toList());
    }
}
