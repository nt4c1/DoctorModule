package com.health.doctor.application.usecase;

import com.health.doctor.domain.model.Appointment;
import com.health.doctor.domain.model.AppointmentStatus;
import com.health.doctor.domain.model.DoctorSchedule;
import com.health.doctor.domain.ports.AppointmentRepositoryPort;
import com.health.doctor.domain.ports.ScheduleRepositoryPort;
import jakarta.inject.Singleton;

import java.time.*;
import java.util.UUID;

@Singleton
public class CreateAppointmentUseCase {
    private static final ZoneId NPT = ZoneId.of("Asia/Kathmandu");

    private final AppointmentRepositoryPort repo;
    private final ScheduleRepositoryPort scheduleRepo;

    public CreateAppointmentUseCase(AppointmentRepositoryPort repo, ScheduleRepositoryPort scheduleRepo) {
        this.repo = repo;
        this.scheduleRepo = scheduleRepo;
    }

    public void execute(UUID doctorId,
                        UUID patientId,
                        LocalDate date,
                        LocalTime time) {

        Instant now = ZonedDateTime.now(NPT).toInstant();

        //Validation against schedule

        DoctorSchedule schedule = scheduleRepo.findByDoctorId(doctorId);

        if(schedule==null){
            throw new RuntimeException("Doctor Has no Schedule Set");
        }
        if (!schedule.isWorkingDay(date.getDayOfWeek())) {
            throw new RuntimeException("Doctor does not work on " + date.getDayOfWeek());
        }
        if (!schedule.isValidSlot(time)) {
            throw new RuntimeException("Time " + time + " is outside working hours or not a valid slot");
        }

        Appointment appointment = new Appointment(
                UUID.randomUUID(),
                doctorId,
                patientId,
                date,
                time,
                AppointmentStatus.PENDING,
                now,
                now
        );

        repo.save(appointment);
    }
}