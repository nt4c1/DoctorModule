package com.health.doctor.adapters.input.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Serdeable
public class CreateScheduleRequest {
    private UUID doctorId;
    private Set<String> workingDays;  // e.g. ["MONDAY","TUESDAY"]
    private LocalTime startTime;
    private LocalTime endTime;
    private int slotDurationMinutes;
    private int maxAppointmentsPerDay;
}