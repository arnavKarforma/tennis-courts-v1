package com.tenniscourts.schedules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT schedule FROM Schedule schedule WHERE schedule.startDateTime > (:startDate) AND schedule.endDateTime < (:endDate)")
    List<Schedule> findSchedules(LocalDateTime startDate, LocalDateTime endDate);

    List<Schedule> findByTennisCourt_IdOrderByStartDateTime(Long id);

}
