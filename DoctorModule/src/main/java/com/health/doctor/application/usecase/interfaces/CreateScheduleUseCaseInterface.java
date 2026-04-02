package com.health.doctor.application.usecase.interfaces;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

public interface CreateScheduleUseCaseInterface {
    public void execute(UUID doctorId, Set<String> workingDays, LocalTime startTime,
                        LocalTime endTime, int slotDuration, int maxPerDay);
}
