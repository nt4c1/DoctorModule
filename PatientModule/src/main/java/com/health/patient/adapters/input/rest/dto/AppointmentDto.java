package com.health.patient.adapters.input.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Serdeable
public class AppointmentDto {
    private String appointmentId;
    private String doctorId;
    private String patientId;
    private String date;
    private String time;
    private String status;
}