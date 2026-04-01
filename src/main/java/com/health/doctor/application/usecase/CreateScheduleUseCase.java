package com.health.doctor.application.usecase;

import com.health.doctor.domain.model.DoctorSchedule;
import com.health.doctor.domain.ports.ScheduleRepositoryPort;
import jakarta.inject.Singleton;

import java.time.*;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class CreateScheduleUseCase {
    private static final ZoneId NPT = ZoneId.of("Asia/Kathmandu");

    private final ScheduleRepositoryPort repo;

    public CreateScheduleUseCase(ScheduleRepositoryPort repo) {
        this.repo = repo;
    }

    Instant now = ZonedDateTime.now(NPT).toInstant();

    public void execute(UUID doctorId, Set<String> workingDays, LocalTime startTime,
                        LocalTime endTime, int slotDuration, int maxPerDay) {
        Set<DayOfWeek> days = workingDays.stream()
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());

        DoctorSchedule schedule = new DoctorSchedule(
                doctorId, days, startTime, endTime, slotDuration, maxPerDay
        );
        repo.save(schedule);
    }
}