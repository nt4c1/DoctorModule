package com.health.doctor.application.usecase;

import com.health.doctor.domain.model.Appointment;
import com.health.doctor.domain.model.AppointmentStatus;
import com.health.doctor.domain.ports.AppointmentRepositoryPort;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Singleton
public class CreateAppointmentUseCase {

    private final AppointmentRepositoryPort repo;

    public CreateAppointmentUseCase(AppointmentRepositoryPort repo) {
        this.repo = repo;
    }

    public void execute(UUID doctorId,
                        UUID patientId,
                        LocalDate date,
                        LocalTime time) {

        Appointment appointment = new Appointment(
                UUID.randomUUID(),
                doctorId,
                patientId,
                date,
                time,
                AppointmentStatus.PENDING,
                Instant.now(),
                Instant.now()
        );

        repo.save(appointment);
    }
}