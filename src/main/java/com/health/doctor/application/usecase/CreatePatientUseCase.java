//TEMPORARY HO
package com.health.doctor.application.usecase;

import com.health.doctor.domain.model.Patient;
import com.health.doctor.domain.ports.PatientRepositoryPort;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class CreatePatientUseCase {
    private final PatientRepositoryPort repo;

    public CreatePatientUseCase(PatientRepositoryPort repo) {
        this.repo = repo;
    }

    public UUID execute(String name) {
        UUID patientId = UUID.randomUUID();

        Patient patient = new Patient(
                patientId,
                name
        );
        repo.save(patient);
        return  patientId;
    }
}