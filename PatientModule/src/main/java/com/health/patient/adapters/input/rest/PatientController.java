package com.health.patient.adapters.input.rest;

import com.health.grpc.doctor.AppointmentMessage;
import com.health.grpc.doctor.DoctorMessage;
import com.health.grpc.doctor.GetScheduleResponse;
import com.health.patient.adapters.input.rest.dto.*;
import com.health.patient.adapters.output.grpc.DoctorGrpcClient;
import com.health.patient.application.CreatePatientUseCase;
import com.health.patient.application.CreatePatientUseCaseInterface;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Controller("/patient")
public class PatientController {

    private final CreatePatientUseCaseInterface createPatient;
    private final DoctorGrpcClient doctorGrpcClient;

    public PatientController(CreatePatientUseCase createPatient,
                             DoctorGrpcClient doctorGrpcClient) {
        this.createPatient = createPatient;
        this.doctorGrpcClient = doctorGrpcClient;
    }

    // Create patient
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post
    public UUID create(@Body CreatePatientRequest req) {
        return createPatient.execute(req.getName());
    }

    // Get nearby doctors
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/doctors/nearby")
    public List<DoctorDto> getNearbyDoctors(@QueryValue String location) {
        return doctorGrpcClient.getNearbyDoctors(location)
                .stream()
                .map(d -> new DoctorDto(
                        d.getDoctorId(),d.getName(),
                        d.getType(),d.getSpecialization(),
                        d.getIsActive()
                )).collect(Collectors.toList())
                ;
    }

    // Get doctors by location (geohash)
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/doctors/location")
    public List<DoctorDto> getDoctorsByLocation(@QueryValue String geohashPrefix) {
        return doctorGrpcClient.getDoctorsByLocation(geohashPrefix)
                .stream()
                .map(d -> new DoctorDto(
                        d.getDoctorId(),d.getName(),
                        d.getType(),d.getSpecialization(),
                        d.getIsActive()
                )).collect(Collectors.toList())
                ;
    }

    // Get doctor schedule
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/doctors/{doctorId}/schedule")
    public ScheduleDto getDoctorSchedule(@PathVariable String doctorId) {
        var schedule = doctorGrpcClient.getDoctorSchedule(doctorId);
        if (schedule == null) return null;
        return new ScheduleDto(
                schedule.getDoctorId(),
                schedule.getWorkingDaysList(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getSlotDurationMinutes(),
                schedule.getMaxAppointmentsDay()
        );
    }

    // Request appointment
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/appointments")
    public String requestAppointment(@Body CreateAppointmentRequest req) {
        boolean success = doctorGrpcClient.createAppointment(
                req.getDoctorId().toString(),
                req.getPatientId().toString(),
                req.getDate().toString(),
                req.getTime().toString()
        );
        return success ? "Appointment booked" : "Booking failed";
    }

    // Cancel appointment
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Delete("/appointments/{appointmentId}")
    public String cancelAppointment(@PathVariable UUID appointmentId,
                                    @QueryValue UUID patientId,
                                    @QueryValue UUID doctorId,
                                    @QueryValue String date,
                                    @QueryValue String time) {
        boolean success = doctorGrpcClient.cancelAppointment(
                appointmentId.toString(), patientId.toString(),
                doctorId.toString(), date, time
        );
        return success ? "Cancelled" : "Cancel failed";
    }

    // My appointments
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/appointments/{patientId}")
    public List<AppointmentDto> getMyAppointments(@PathVariable UUID patientId,
                                                  @QueryValue String date) {
        return doctorGrpcClient.getMyAppointments(patientId.toString(), date)
                .stream()
                .map(a -> new AppointmentDto(
                        a.getAppointmentId(),a.getDoctorId(),
                        a.getPatientId(),a.getDate(),
                        a.getTime(),a.getStatus()
                )).collect(Collectors.toList())
                ;
    }
}