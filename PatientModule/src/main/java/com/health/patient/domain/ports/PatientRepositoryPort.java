package com.health.patient.domain.ports;

import com.health.patient.domain.model.Patient;

import java.util.UUID;

public interface PatientRepositoryPort {
    void save(Patient patient);
    Patient findById(UUID patientId);
}
