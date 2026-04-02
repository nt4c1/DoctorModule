package com.health.patient.application;

import com.health.patient.domain.model.Patient;
import com.health.patient.domain.ports.PatientRepositoryPort;
import jakarta.inject.Singleton;
import java.util.UUID;

@Singleton
public class CreatePatientUseCase implements CreatePatientUseCaseInterface{
    private final PatientRepositoryPort repo;

    public CreatePatientUseCase(PatientRepositoryPort repo) {
        this.repo = repo;
    }

    public UUID execute(String name) {
        UUID id = UUID.randomUUID();
        repo.save(new Patient(id, name));
        return id;
    }
}