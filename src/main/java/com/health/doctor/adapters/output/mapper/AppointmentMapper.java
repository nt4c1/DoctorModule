package com.health.doctor.adapters.output.mapper;

import com.health.doctor.adapters.output.persistence.entity.AppointmentByDoctorEntity;
import com.health.doctor.adapters.output.persistence.entity.AppointmentByDoctorStatusEntity;
import com.health.doctor.domain.model.Appointment;

import java.time.Instant;

public class AppointmentMapper {

    public static AppointmentByDoctorEntity toDoctorEntity(Appointment a) {
        AppointmentByDoctorEntity e = new AppointmentByDoctorEntity();
        e.setDoctorId(a.getDoctorId());
        e.setAppointmentDate(a.getAppointmentDate());
        e.setScheduledTime(a.getScheduleTime());
        e.setAppointmentId(a.getId());
        e.setPatientId(a.getPatientId());
        e.setStatus(a.getStatus().name());
        e.setCreatedAt(Instant.now());
        e.setUpdatedAt(Instant.now());
        return e;
    }

    public static AppointmentByDoctorStatusEntity toStatusEntity(Appointment a) {
        AppointmentByDoctorStatusEntity e = new AppointmentByDoctorStatusEntity();
        e.setDoctorId(a.getDoctorId());
        e.setStatus(a.getStatus().name());
        e.setAppointmentDate(a.getAppointmentDate());
        e.setScheduledTime(a.getScheduleTime());
        e.setAppointmentId(a.getId());
        e.setPatientId(a.getPatientId());
        return e;
    }


}