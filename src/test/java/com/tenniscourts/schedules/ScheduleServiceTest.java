package com.tenniscourts.schedules;

import com.tenniscourts.tenniscourts.TennisCourtRepository;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleServiceTest  {

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleMapper scheduleMapper;

    @Mock
    private TennisCourtRepository tennisCourtRepository;
}
