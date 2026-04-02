package com.health.patient.adapters.input.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Serdeable
public class CreateAppointmentRequest {
    private UUID doctorId;
    private UUID patientId;
    private LocalDate date;
    private LocalTime time;
}