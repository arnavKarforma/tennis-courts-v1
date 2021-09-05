package com.tenniscourts.reservations;

import com.tenniscourts.schedules.Schedule;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationServiceImpl.class)
public class ReservationServiceTest {

    @InjectMocks
    private
    ReservationServiceImpl reservationServiceImpl;

    @Test
    public void getRefundValueFullRefund() {
        final Schedule schedule = new Schedule();

        final LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(this.reservationServiceImpl.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal(10));
    }
}