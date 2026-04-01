package com.health.doctor.adapters.input.rest.dto;

import com.health.doctor.domain.model.DoctorType;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Serdeable
@Getter
@Setter
public class CreateDoctorRequest {

    private String name;
    private UUID clinicId;
    private DoctorType type;
    private String specialization;

}
