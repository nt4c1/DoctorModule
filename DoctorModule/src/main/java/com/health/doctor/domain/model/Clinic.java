package com.health.doctor.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class Clinic {
    private UUID id;
    private String name;
    private Location location;
    private boolean isActive;

    public Clinic(UUID id, String name, Location location, boolean isActive) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.isActive = isActive;
    }

}
