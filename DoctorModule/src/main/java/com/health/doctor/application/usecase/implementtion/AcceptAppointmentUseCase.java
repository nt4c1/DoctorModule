package com.health.doctor.application.usecase.implementtion;

import com.health.doctor.application.usecase.interfaces.AcceptAppointmentUseCaseInterface;
import com.health.doctor.domain.model.Appointment;
import com.health.doctor.domain.model.AppointmentStatus;
import com.health.doctor.domain.ports.AppointmentRepositoryPort;
import jakarta.inject.Singleton;

@Singleton
public class AcceptAppointmentUseCase implements AcceptAppointmentUseCaseInterface {
    private final AppointmentRepositoryPort repo;

    public AcceptAppointmentUseCase(AppointmentRepositoryPort repo) {
        this.repo = repo;
    }

    public void execute (Appointment appointment){
        repo.updateStatus(appointment, AppointmentStatus.ACCEPTED.name());
    }
}
