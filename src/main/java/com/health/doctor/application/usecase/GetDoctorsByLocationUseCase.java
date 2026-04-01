package com.health.doctor.application.usecase;

import com.health.doctor.domain.model.Doctor;
import com.health.doctor.domain.ports.DoctorRepositoryPort;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class GetDoctorsByLocationUseCase {

    private final DoctorRepositoryPort repo;

    public GetDoctorsByLocationUseCase(DoctorRepositoryPort repo) {
        this.repo = repo;
    }

    public List<Doctor> execute(String geohashPrefix) {
        return repo.findByGeohashPrefix(geohashPrefix);
    }
}