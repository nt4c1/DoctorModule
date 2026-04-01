package com.health.doctor.adapters.input.rest;

import com.health.doctor.adapters.input.rest.dto.*;
import com.health.doctor.application.usecase.*;
import com.health.doctor.domain.model.Appointment;
import com.health.doctor.domain.model.AppointmentStatus;
import io.micronaut.http.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller("/appointments")
public class AppointmentController {

    private final CreateAppointmentUseCase createUseCase;
    private final GetAppointmentsUseCase getUseCase;
    private final GetPendingAppointmentsUseCase pendingUseCase;
    private final AcceptAppointmentUseCase acceptUseCase;
    private final PostponeAppointmentUseCase postponeUseCase;

    public AppointmentController(
            CreateAppointmentUseCase createUseCase,
            GetAppointmentsUseCase getUseCase,
            GetPendingAppointmentsUseCase pendingUseCase,
            AcceptAppointmentUseCase acceptUseCase,
            PostponeAppointmentUseCase postponeUseCase) {

        this.createUseCase = createUseCase;
        this.getUseCase = getUseCase;
        this.pendingUseCase = pendingUseCase;
        this.acceptUseCase = acceptUseCase;
        this.postponeUseCase = postponeUseCase;
    }

    // Create Appointment
    @Post
    public void create(@Body CreateAppointmentRequest request) {

        createUseCase.execute(
                request.getDoctorId(),
                request.getPatientId(),
                request.getDate(),
                request.getTime()
        );
    }

    // Get All Appointments
    @Get("/{doctorId}")
    public List<Appointment> get(
            @PathVariable UUID doctorId,
            @QueryValue LocalDate date) {

        return getUseCase.execute(doctorId, date);
    }

    //  Get Pending
    @Get("/{doctorId}/pending")
    public List<Appointment> getPending(
            @PathVariable UUID doctorId,
            @QueryValue LocalDate date) {

        return pendingUseCase.execute(doctorId, date);
    }

    // Accept
    @Post("/accept")
    public void accept(@Body UpdateStatusRequest req) {

        Appointment a = map(req);

        acceptUseCase.execute(a);
    }

    // Postpone
    @Post("/postpone")
    public void postpone(@Body UpdateStatusRequest req) {

        Appointment a = map(req);

        postponeUseCase.execute(a);
    }

    private Appointment map(UpdateStatusRequest r) {
        return new Appointment(
                r.getAppointmentId(),
                r.getDoctorId(),
                r.getPatientId(),
                r.getDate(),
                r.getTime(),
                AppointmentStatus.valueOf(r.getCurrentStatus()),
                null,
                null
        );
    }
}