package com.health.doctor.adapters.input.rest.dto;

import com.health.doctor.domain.model.DoctorType;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Serdeable
public class DoctorResponse {
    private UUID id;
    private String name;
    private String specialization;
    private UUID clinicId;
}
