package com.health.doctor.adapters.input.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.Setter;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


@Serdeable
@Getter
@Setter
public class CreateAppointmentRequest {

    private UUID doctorId;
    private UUID patientId;
    private LocalDate date;
    private Instant time;

}