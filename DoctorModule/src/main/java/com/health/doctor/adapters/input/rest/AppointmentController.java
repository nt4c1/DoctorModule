package com.health.doctor.adapters.input.rest;

import com.health.doctor.adapters.input.rest.dto.*;
import com.health.doctor.application.usecase.implementtion.*;
import com.health.doctor.application.usecase.interfaces.*;
import com.health.doctor.domain.model.Appointment;
import com.health.doctor.domain.model.AppointmentStatus;
import com.health.doctor.domain.ports.AppointmentRepositoryPort;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller("/appointments")
public class AppointmentController {

    private final CreateAppointmentUseCaseInterface createUseCase;
    private final GetAppointmentsUseCaseInterface getUseCase;
    private final GetPendingAppointmentsUseCaseInterface pendingUseCase;
    private final AcceptAppointmentUseCaseInterface acceptUseCase;
    private final PostponeAppointmentUseCaseInterface postponeUseCase;
    private final AppointmentRepositoryPort appointmentRepo;

    public AppointmentController(CreateAppointmentUseCase createUseCase,
                                 GetAppointmentsUseCase getUseCase,
                                 GetPendingAppointmentsUseCase pendingUseCase,
                                 AcceptAppointmentUseCase acceptUseCase,
                                 PostponeAppointmentUseCase postponeUseCase,
                                 AppointmentRepositoryPort appointmentRepo) {
        this.createUseCase = createUseCase;
        this.getUseCase = getUseCase;
        this.pendingUseCase = pendingUseCase;
        this.acceptUseCase = acceptUseCase;
        this.postponeUseCase = postponeUseCase;
        this.appointmentRepo = appointmentRepo;
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post
    public void create(@Body CreateAppointmentRequest request) {
        createUseCase.execute(request.getDoctorId(), request.getPatientId(),
                request.getDate(), request.getTime());
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/{doctorId}")
    public List<Appointment> get(@PathVariable UUID doctorId, @QueryValue LocalDate date) { //path variable le raw value use garne queryvalue le attribute= ma line
        return getUseCase.execute(doctorId, date);
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/{doctorId}/pending")
    public List<Appointment> getPending(@PathVariable UUID doctorId, @QueryValue LocalDate date) {
        return pendingUseCase.execute(doctorId, date);
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/status/accept")
    public void accept(@Body UpdateStatusRequest req) {
        acceptUseCase.execute(map(req));
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/status/postpone")
    public void postpone(@Body UpdateStatusRequest req) {
        postponeUseCase.execute(map(req));
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Delete("/{appointmentId}")
    public void cancel(@PathVariable UUID appointmentId,
                       @QueryValue UUID patientId,
                       @QueryValue UUID doctorId,
                       @QueryValue LocalDate date,
                       @QueryValue String time) {
        appointmentRepo.cancel(appointmentId, patientId, doctorId, date,
                java.time.LocalTime.parse(time));
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/patient/{patientId}")
    public List<Appointment> getMyAppointments(@PathVariable UUID patientId,
                                               @QueryValue LocalDate date) {
        return appointmentRepo.findByPatientAndDate(patientId, date);
    }

    private Appointment map(UpdateStatusRequest r) {
        return new Appointment(r.getAppointmentId(), r.getDoctorId(), r.getPatientId(),
                r.getDate(), r.getTime(), AppointmentStatus.valueOf(r.getCurrentStatus()),
                null, null);
    }
}