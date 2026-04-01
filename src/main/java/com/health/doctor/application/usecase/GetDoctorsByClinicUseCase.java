package com.health.doctor.application.usecase;

import com.health.doctor.domain.model.Doctor;
import com.health.doctor.domain.ports.DoctorRepositoryPort;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

@Singleton
public class GetDoctorsByClinicUseCase {

    private final DoctorRepositoryPort repo;

    public GetDoctorsByClinicUseCase(DoctorRepositoryPort repo) {
        this.repo = repo;
    }

    public List<Doctor> execute(UUID clinicId) {
        return repo.findByClinicId(clinicId);
    }
}