package com.health.doctor.adapters.input.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.Setter;

@Serdeable
@Getter
@Setter
public class CreateClinicRequest {

    private String name;
    private String locationText;

}