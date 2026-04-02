package com.health.doctor.domain.model;

import java.util.UUID;

public class Doctor {
    private UUID id;
    private String name;
    private UUID clinicId;
    private DoctorType type;
    private String specialization;
    private boolean isActive;

    public Doctor(UUID id, String name, UUID clinicId, DoctorType type, String specialization, boolean isActive) {
        this.id = id;
        this.name = name;
        this.clinicId = clinicId;
        this.type = type;
        this.specialization = specialization;
        this.isActive = isActive;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getClinicId() {
        return clinicId;
    }

    public void setClinicId(UUID clinicId) {
        this.clinicId = clinicId;
    }

    public DoctorType getType() {
        return type;
    }

    public void setType(DoctorType type) {
        this.type = type;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
