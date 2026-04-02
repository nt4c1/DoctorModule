package com.health.patient.adapters.input.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Serdeable
public class DoctorDto {
    private String doctorId;
    private String name;
    private String type;
    private String specialization;
    private boolean isActive;
}