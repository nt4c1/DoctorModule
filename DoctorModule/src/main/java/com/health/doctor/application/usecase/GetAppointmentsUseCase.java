package com.health.doctor.application.usecase;

import com.health.doctor.domain.model.Appointment;
import com.health.doctor.domain.ports.AppointmentRepositoryPort;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Singleton
public class GetAppointmentsUseCase {
    private final AppointmentRepositoryPort repo;

    public GetAppointmentsUseCase(AppointmentRepositoryPort repo) {
        this.repo = repo;
    }

    public List<Appointment> execute(UUID doctorId, LocalDate date){
        return repo.findByDoctorAndDate(doctorId, date);
    }
}
