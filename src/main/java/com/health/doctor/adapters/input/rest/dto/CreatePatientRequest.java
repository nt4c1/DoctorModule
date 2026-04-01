package com.health.doctor.adapters.input.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Serdeable
@AllArgsConstructor
@NoArgsConstructor
@Serdeable.Deserializable
@Getter
@Setter
public class CreatePatientRequest {
    private String name;
    private UUID id;
}
