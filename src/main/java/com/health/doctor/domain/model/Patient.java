//Temporary

package com.health.doctor.domain.model;

import java.util.UUID;

public class Patient {

    private UUID id;
    private String name;

    public Patient(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
}