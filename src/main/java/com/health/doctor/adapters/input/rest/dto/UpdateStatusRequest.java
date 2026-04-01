package com.health.doctor.adapters.input.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Serdeable
public class UpdateStatusRequest {

    private UUID appointmentId;
    private UUID doctorId;
    private LocalDate date;
    private LocalTime time;
    private UUID patientId;
    private String currentStatus;

}