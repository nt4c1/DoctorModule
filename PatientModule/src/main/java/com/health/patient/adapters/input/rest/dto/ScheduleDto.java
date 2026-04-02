package com.health.patient.adapters.input.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
@Serdeable
public class ScheduleDto {
    private String doctorId;
    private List<String> workingDays;
    private String startTime;
    private String endTime;
    private int slotDurationMinutes;
    private int maxAppointmentsDay;
}