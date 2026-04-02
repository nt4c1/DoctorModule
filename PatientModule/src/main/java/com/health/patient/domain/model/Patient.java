package com.health.patient.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Patient {
    private UUID id;
    private String name;

    public Patient(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}