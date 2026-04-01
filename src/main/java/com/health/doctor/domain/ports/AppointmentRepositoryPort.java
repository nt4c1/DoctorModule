package com.health.doctor.domain.ports;

import com.health.doctor.domain.model.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepositoryPort {
    void save (Appointment appointment);
    List<Appointment> findByDoctorAndDate (UUID doctorID, LocalDate date);
    List<Appointment> findDoctorAndStatus (UUID doctorId, String status, LocalDate date);
    List<Appointment> findPending (UUID doctorId,LocalDate date);
    void updateStatus(Appointment a ,String newStatus);
    void decrementCount(UUID doctorId,LocalDate date);
}
