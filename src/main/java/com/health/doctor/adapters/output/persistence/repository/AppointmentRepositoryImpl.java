package com.health.doctor.adapters.output.persistence.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.health.doctor.domain.model.Appointment;
import com.health.doctor.domain.model.AppointmentStatus;
import com.health.doctor.domain.ports.AppointmentRepositoryPort;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Singleton
public class AppointmentRepositoryImpl implements AppointmentRepositoryPort {

    private final CqlSession session;

    public AppointmentRepositoryImpl(CqlSession session) {
        this.session = session;
    }

    @Override
    public void save(Appointment a) {

        // 🔥 LWT: enforce max 50/day
        ResultSet rs = session.execute(
                "UPDATE doctor_service.appointment_count_by_doctor_date " +
                        "SET count = count + 1 " +
                        "WHERE doctor_id=? AND appointment_date=? IF count < 50",
                a.getDoctorId(), a.getAppointmentDate()
        );

        if (!rs.wasApplied()) {
            throw new RuntimeException("Doctor fully booked for the day");
        }

        try {
            // main table
            session.execute(
                    "INSERT INTO doctor_service.appointments_by_doctor " +
                            "(doctor_id, appointment_date, scheduled_time, appointment_id, patient_id, status, created_at, updated_at) " +
                            "VALUES (?,?,?,?,?,?,?,?)",
                    a.getDoctorId(),
                    a.getAppointmentDate(),
                    a.getScheduleTime(),
                    a.getId(),
                    a.getPatientId(),
                    a.getStatus().name(),
                    a.getCreatedAt(),
                    a.getUpdateAt()
            );

            // status table
            session.execute(
                    "INSERT INTO doctor_service.appointments_by_doctor_status " +
                            "(doctor_id, status, appointment_date, scheduled_time, appointment_id, patient_id) " +
                            "VALUES (?,?,?,?,?,?)",
                    a.getDoctorId(),
                    a.getStatus().name(),
                    a.getAppointmentDate(),
                    a.getScheduleTime(),
                    a.getId(),
                    a.getPatientId()
            );

        } catch (Exception e) {
            decrementCount(a.getDoctorId(), a.getAppointmentDate());
            throw e;
        }
    }

    @Override
    public List<Appointment> findByDoctorAndDate(UUID doctorId, LocalDate date) {

        ResultSet rs = session.execute(
                "SELECT * FROM doctor_service.appointments_by_doctor WHERE doctor_id=? AND appointment_date=?",
                doctorId, date
        );

        List<Appointment> list = new ArrayList<>();

        for (Row r : rs) {
            list.add(mapRow(r));
        }

        return list;
    }

    @Override
    public List<Appointment> findDoctorAndStatus(UUID doctorId, String status, LocalDate date) {

        ResultSet rs = session.execute(
                "SELECT * FROM doctor_service.appointments_by_doctor_status WHERE doctor_id=? AND status=? AND appointment_date=?",
                doctorId, status, date
        );

        List<Appointment> list = new ArrayList<>();

        for (Row r : rs) {
            list.add(new Appointment(
                    r.getUuid("appointment_id"),
                    r.getUuid("doctor_id"),
                    r.getUuid("patient_id"),
                    r.getLocalDate("appointment_date"),
                    r.getLocalTime("scheduled_time"),
                    AppointmentStatus.valueOf(status),
                    null,
                    null
            ));
        }

        return list;
    }

    @Override
    public List<Appointment> findPending(UUID doctorId, LocalDate date) {
        return findDoctorAndStatus(doctorId, "PENDING", date);
    }

    @Override
    public void updateStatus(Appointment a, String newStatus) {

        // Delete old row (can't update primary key in Cassandra)
        session.execute(
                "DELETE FROM doctor_service.appointments_by_doctor " +
                        "WHERE doctor_id=? AND appointment_date=? AND scheduled_time=? AND appointment_id=?",
                a.getDoctorId(),
                a.getAppointmentDate().minusDays(1), // ← old date
                a.getScheduleTime(),
                a.getId()
        );

        // Insert new row with new date
        session.execute(
                "INSERT INTO doctor_service.appointments_by_doctor " +
                        "(doctor_id, appointment_date, scheduled_time, appointment_id, patient_id, status, created_at, updated_at) " +
                        "VALUES (?,?,?,?,?,?,?,?)",
                a.getDoctorId(),
                a.getAppointmentDate(), // ← new date
                a.getScheduleTime(),
                a.getId(),
                a.getPatientId(),
                newStatus,
                a.getCreatedAt(),
                ZonedDateTime.now(ZoneId.of("Asia/Kathmandu")).toInstant()
        );

        // Delete old status row
        session.execute(
                "DELETE FROM doctor_service.appointments_by_doctor_status " +
                        "WHERE doctor_id=? AND status=? AND appointment_date=? AND scheduled_time=? AND appointment_id=?",
                a.getDoctorId(),
                a.getStatus().name(),
                a.getAppointmentDate().minusDays(1), // ← old date
                a.getScheduleTime(),
                a.getId()
        );

        // Insert new status row with new date
        session.execute(
                "INSERT INTO doctor_service.appointments_by_doctor_status " +
                        "(doctor_id, status, appointment_date, scheduled_time, appointment_id, patient_id) " +
                        "VALUES (?,?,?,?,?,?)",
                a.getDoctorId(),
                newStatus,
                a.getAppointmentDate(), // ← new date
                a.getScheduleTime(),
                a.getId(),
                a.getPatientId()
        );
    }

    @Override
    public void decrementCount(UUID doctorId, LocalDate date) {
        session.execute(
                "UPDATE doctor_service.appointment_count_by_doctor_date " +
                        "SET count = count - 1 " +
                        "WHERE doctor_id=? AND appointment_date=?",
                doctorId, date
        );
    }

    private Appointment mapRow(Row r) {
        return new Appointment(
                r.getUuid("appointment_id"),
                r.getUuid("doctor_id"),
                r.getUuid("patient_id"),
                r.getLocalDate("appointment_date"),
                r.getLocalTime("scheduled_time"),
                AppointmentStatus.valueOf(r.getString("status")),
                r.getInstant("created_at"),
                r.getInstant("updated_at")
        );
    }
}