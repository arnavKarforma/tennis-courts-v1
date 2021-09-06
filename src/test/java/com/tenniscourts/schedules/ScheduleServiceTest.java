package com.tenniscourts.schedules;

import com.tenniscourts.TestDataUtilities;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    public void addTennisCourtSuccess() {
        final Schedule scheduleInDb = TestDataUtilities.getTestSchedule();
        final Schedule scheduleBeSaved = TestDataUtilities.getTestSchedule();
        final CreateScheduleRequestDTO scheduleDTORequest = TestDataUtilities.getTestScheduleDTO();
        final ScheduleDTO ScheduleResponseDTO = TestDataUtilities.getTestScheduleResponseDTO();
        scheduleBeSaved.setId(null);
        final TennisCourt tennisCourt = TestDataUtilities.getTestTennisCourt();
        when(this.tennisCourtRepository.findById(1L)).thenReturn(Optional.of(tennisCourt));
        when(this.scheduleRepository.save(any(Schedule.class))).thenReturn(scheduleInDb);
        when(this.scheduleMapper.map(any(Schedule.class))).thenReturn(ScheduleResponseDTO);

        final ScheduleDTO scheduleDTORes = this.scheduleService.addSchedule(1L, scheduleDTORequest);

        Assert.assertEquals(scheduleDTORes.getId(), scheduleInDb.getId());
        Assert.assertEquals(scheduleDTORes.getTennisCourt().getId(), tennisCourt.getId());
        Assert.assertEquals(scheduleDTORes.getTennisCourt().getName(), tennisCourt.getName());
        Assert.assertEquals(scheduleDTORes.getStartDateTime(), scheduleDTORequest.getStartDateTime());

        scheduleInDb.setId(null);
        //Todo: Add argument captor to check the values
        verify(this.scheduleRepository).save(any(Schedule.class));
        verify(this.scheduleMapper).map(any(Schedule.class));
        verify(this.tennisCourtRepository).findById(1L);

    }

    @Test
    public void addTennisCourtFailureTennisCourtNotFound() {
        final Schedule scheduleInDb = TestDataUtilities.getTestSchedule();
        final Schedule scheduleBeSaved = TestDataUtilities.getTestSchedule();
        final CreateScheduleRequestDTO scheduleDTORequest = TestDataUtilities.getTestScheduleDTO();
        final ScheduleDTO ScheduleResponseDTO = TestDataUtilities.getTestScheduleResponseDTO();
        scheduleBeSaved.setId(null);

        when(this.tennisCourtRepository.findById(1L)).thenReturn(Optional.empty());
        when(this.scheduleRepository.save(any(Schedule.class))).thenReturn(scheduleInDb);
        when(this.scheduleMapper.map(any(Schedule.class))).thenReturn(ScheduleResponseDTO);

        final Throwable throwable = catchThrowable(()-> this.scheduleService.addSchedule(1L, scheduleDTORequest));

        Assert.assertNotEquals(null, throwable);
        Assert.assertTrue(throwable instanceof EntityNotFoundException);

        verify(this.scheduleRepository, never()).save(any(Schedule.class));
        verify(this.scheduleMapper, never()).map(any(Schedule.class));
        verify(this.tennisCourtRepository).findById(1L);

    }

    @Test
    public void findSchedulesByIdSuccess() {
        final Schedule ScheduleInDb = TestDataUtilities.getTestSchedule();
        final ScheduleDTO scheduleDTO = TestDataUtilities.getTestScheduleResponseDTO();

        when(this.scheduleRepository.findById(1L)).thenReturn(java.util.Optional.of(ScheduleInDb));
        when(this.scheduleMapper.map(ScheduleInDb)).thenReturn(scheduleDTO);

        final ScheduleDTO scheduleDTOTRes = this.scheduleService.findSchedule(1L);

        Assert.assertEquals(1L, scheduleDTOTRes.getId().longValue());
        Assert.assertEquals(scheduleDTOTRes.getStartDateTime(), scheduleDTO.getStartDateTime());

        verify(this.scheduleRepository).findById(1L);
        verify(this.scheduleMapper).map(ScheduleInDb);

    }

}
