package com.tenniscourts.tenniscourts;

import com.tenniscourts.TestDataUtilities;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.schedules.ScheduleServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TennisCourtServiceTest {

    @InjectMocks
    private TennisCourtServiceImpl tennisCourtService;

    @Mock
    private TennisCourtRepository tennisCourtRepository;

    @Mock
    private ScheduleServiceImpl scheduleService;

    @Mock
    private TennisCourtMapper tennisCourtMapper;

    @Test
    public void addTennisCourtSuccess() {
        final TennisCourt tennisCourtInDb = TestDataUtilities.getTestTennisCourt();
        final TennisCourt tennisCourtBeSaved = TestDataUtilities.getTestTennisCourt();
        final TennisCourtDTO tennisCourtDTO = TestDataUtilities.getTestTennisCourtResponseDTO();
        final TennisCourtDTO tennisCourtDTORequest = TestDataUtilities.getTestTennisCourtDTO();
        tennisCourtBeSaved.setId(null);

        when(this.tennisCourtMapper.map(tennisCourtDTORequest)).thenReturn(tennisCourtBeSaved);
        when(this.tennisCourtRepository.save(tennisCourtBeSaved)).thenReturn(tennisCourtInDb);
        when(this.tennisCourtMapper.map(tennisCourtInDb)).thenReturn(tennisCourtDTO);

        final TennisCourtDTO tennisCourtDTORes = this.tennisCourtService.addTennisCourt(tennisCourtDTORequest);

        Assert.assertEquals(tennisCourtDTORes.getId(), tennisCourtInDb.getId());
        Assert.assertEquals(tennisCourtDTORes.getName(), tennisCourtInDb.getName());
        tennisCourtInDb.setId(null);
        verify(this.tennisCourtRepository).save(tennisCourtInDb);
        verify(this.tennisCourtMapper).map(tennisCourtDTORequest);
        verify(this.tennisCourtMapper).map(tennisCourtBeSaved);
    }

    @Test
    public void findTennisCourtByIdSuccess() {
        final TennisCourt tennisCourtInDb = TestDataUtilities.getTestTennisCourt();
        final TennisCourtDTO tennisCourtDTO = TestDataUtilities.getTestTennisCourtResponseDTO();

        when(this.tennisCourtRepository.findById(1L)).thenReturn(java.util.Optional.of(tennisCourtInDb));
        when(this.tennisCourtMapper.map(tennisCourtInDb)).thenReturn(tennisCourtDTO);

        final TennisCourtDTO tennisCourtDTORes = this.tennisCourtService.findTennisCourtById(1L);

        Assert.assertEquals(tennisCourtDTO.getId(), tennisCourtDTORes.getId());
        Assert.assertEquals(tennisCourtDTO.getName(), tennisCourtDTORes.getName());

        verify(this.tennisCourtRepository).findById(1L);
        verify(this.tennisCourtMapper).map(tennisCourtInDb);

    }

    @Test
    public void findTennisCourtByIdFailure() {
        final TennisCourt tennisCourtInDb = TestDataUtilities.getTestTennisCourt();

        when(this.tennisCourtRepository.findById(1L)).thenReturn(Optional.empty());

        final Throwable throwable = catchThrowable(() -> this.tennisCourtService.findTennisCourtById(1L));

        Assert.assertNotEquals(null, throwable);
        Assert.assertTrue(throwable instanceof EntityNotFoundException);

        verify(this.tennisCourtRepository).findById(1L);
        verify(this.tennisCourtMapper, never()).map(tennisCourtInDb);

    }

    @Test
    public void findTennisCourtWithSchedulesByIdSuccess() {
        final TennisCourt tennisCourtInDb = TestDataUtilities.getTestTennisCourt();
        final TennisCourtDTO tennisCourtDTO = TestDataUtilities.getTestTennisCourtResponseDTO();
        final List<ScheduleDTO> schedules = List.of(TestDataUtilities.getTestScheduleResponseDTO());

        when(this.tennisCourtRepository.findById(1L)).thenReturn(java.util.Optional.of(tennisCourtInDb));
        when(this.tennisCourtMapper.map(tennisCourtInDb)).thenReturn(tennisCourtDTO);
        when(this.scheduleService.findSchedulesByTennisCourtId(1L)).thenReturn(schedules);

        final TennisCourtDTO tennisCourtDTORes = this.tennisCourtService.findTennisCourtWithSchedulesById(1L);

        Assert.assertEquals(tennisCourtDTO.getId(), tennisCourtDTORes.getId());
        Assert.assertEquals(tennisCourtDTO.getName(), tennisCourtDTORes.getName());
        Assert.assertEquals(schedules, tennisCourtDTORes.getTennisCourtSchedules());

        verify(this.tennisCourtRepository).findById(1L);
        verify(this.tennisCourtMapper).map(tennisCourtInDb);
        verify(this.scheduleService).findSchedulesByTennisCourtId(1L);

    }

    @Test
    public void listAllTennisCourtsSuccess() {
        final TennisCourt tennisCourtInDb = TestDataUtilities.getTestTennisCourt();
        final TennisCourtDTO tennisCourtDTO = TestDataUtilities.getTestTennisCourtResponseDTO();

        when(this.tennisCourtRepository.findAll()).thenReturn(List.of(tennisCourtInDb));
        when(this.tennisCourtMapper.map(tennisCourtInDb)).thenReturn(tennisCourtDTO);

        final List<TennisCourtDTO> tennisCourtDTORes = this.tennisCourtService.listAllTennisCourts();

        Assert.assertEquals(tennisCourtDTO.getId(), tennisCourtDTORes.get(0).getId());
        Assert.assertEquals(tennisCourtDTO.getName(), tennisCourtDTORes.get(0).getName());

        verify(this.tennisCourtRepository).findAll();
        verify(this.tennisCourtMapper).map(tennisCourtInDb);

    }
}
