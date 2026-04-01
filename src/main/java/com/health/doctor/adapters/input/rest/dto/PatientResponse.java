package com.health.doctor.adapters.input.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Serdeable
@Serdeable.Deserializable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {
    private UUID id;
    private String name;
}
