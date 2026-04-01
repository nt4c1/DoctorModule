package com.health.doctor.application.usecase;

import com.health.doctor.domain.model.DoctorSchedule;
import com.health.doctor.domain.ports.ScheduleRepositoryPort;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class GetScheduleUseCase {
    private final ScheduleRepositoryPort repo;

    public GetScheduleUseCase(ScheduleRepositoryPort repo) {
        this.repo = repo;
    }

    public DoctorSchedule execute(UUID doctorId) {
        return repo.findByDoctorId(doctorId);
    }
}