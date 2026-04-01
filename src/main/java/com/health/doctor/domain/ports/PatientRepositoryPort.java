package com.health.doctor.domain.ports;

import com.health.doctor.domain.model.Patient;
import java.util.UUID;

public interface PatientRepositoryPort {
    void save(Patient patient);
    Patient findById(UUID patientId);
}