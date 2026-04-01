package com.health.doctor.application.usecase;

import com.health.doctor.domain.model.Appointment;
import com.health.doctor.domain.model.AppointmentStatus;
import com.health.doctor.domain.ports.AppointmentRepositoryPort;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class AcceptAppointmentUseCase {
    private final AppointmentRepositoryPort repo;

    public AcceptAppointmentUseCase(AppointmentRepositoryPort repo) {
        this.repo = repo;
    }

    public void execute (Appointment appointment){
        repo.updateStatus(appointment, AppointmentStatus.ACCEPTED.name());
    }
}
