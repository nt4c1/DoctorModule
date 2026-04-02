package com.health.doctor.application.usecase.interfaces;

import com.health.doctor.domain.model.DoctorSchedule;

import java.util.UUID;

public interface GetScheduleUseCaseInterface {
    DoctorSchedule execute(UUID doctorId);
}
