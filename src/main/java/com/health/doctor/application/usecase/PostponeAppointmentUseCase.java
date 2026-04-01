package com.health.doctor.application.usecase;

import com.health.doctor.domain.model.Appointment;
import com.health.doctor.domain.model.AppointmentStatus;
import com.health.doctor.domain.ports.AppointmentRepositoryPort;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class PostponeAppointmentUseCase {
    private final AppointmentRepositoryPort repo;

    public PostponeAppointmentUseCase(AppointmentRepositoryPort repo) {
        this.repo = repo;
    }

    public void execute(Appointment appointment){
        repo.updateStatus(appointment, AppointmentStatus.POSTPONED.name());
    }
}
