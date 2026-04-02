package com.health.doctor.adapters.input.rest;

import com.health.doctor.adapters.input.rest.dto.CreateScheduleRequest;
import com.health.doctor.application.usecase.implementtion.CreateScheduleUseCase;
import com.health.doctor.application.usecase.implementtion.GetScheduleUseCase;
import com.health.doctor.application.usecase.interfaces.CreateScheduleUseCaseInterface;
import com.health.doctor.application.usecase.interfaces.GetScheduleUseCaseInterface;
import com.health.doctor.domain.model.DoctorSchedule;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.UUID;

@Controller("/schedule")
public class ScheduleController {

    private final CreateScheduleUseCaseInterface createSchedule;
    private final GetScheduleUseCaseInterface getSchedule;

    public ScheduleController(CreateScheduleUseCase createSchedule, GetScheduleUseCase getSchedule) {
        this.createSchedule = createSchedule;
        this.getSchedule = getSchedule;
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post
    public void create(@Body CreateScheduleRequest req) {
        createSchedule.execute(
                req.getDoctorId(),
                req.getWorkingDays(),
                req.getStartTime(),
                req.getEndTime(),
                req.getSlotDurationMinutes(),
                req.getMaxAppointmentsPerDay()
        );
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/{doctorId}")
    public DoctorSchedule get(@PathVariable UUID doctorId) {
        return getSchedule.execute(doctorId);
    }
}