package com.health.doctor.application.usecase;

import com.health.doctor.domain.model.Doctor;
import com.health.doctor.domain.model.DoctorType;
import com.health.doctor.domain.ports.DoctorRepositoryPort;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class CreateDoctorUseCase {

    private final DoctorRepositoryPort repo;

    public CreateDoctorUseCase(DoctorRepositoryPort repo) {
        this.repo = repo;
    }

    public UUID execute(String name,
                        UUID clinicId,
                        DoctorType type,
                        String specialization) {

        UUID doctorId = UUID.randomUUID();

        Doctor doctor = new Doctor(
                doctorId,
                name,
                clinicId,
                type,
                specialization,
                true
        );

        repo.save(doctor);

        return doctorId;
    }
}