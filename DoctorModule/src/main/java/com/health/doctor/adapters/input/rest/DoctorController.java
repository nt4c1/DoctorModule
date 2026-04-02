package com.health.doctor.adapters.input.rest;

import com.health.doctor.adapters.input.rest.dto.*;
import com.health.doctor.application.usecase.*;
import com.health.doctor.domain.model.Doctor;
import com.health.doctor.domain.model.DoctorType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.List;
import java.util.UUID;

@Controller("/doctor")
public class DoctorController {

    private final CreateDoctorUseCase createDoctor;
    private final CreateClinicUseCase createClinic;
    private final UpdateDoctorLocationUseCase updateLocation;
    private final GetDoctorsByLocationUseCase searchUseCase;
    private final GetDoctorsByClinicUseCase clinicUseCase;
    private final CreateScheduleUseCase createSchedule;

    public DoctorController(CreateDoctorUseCase createDoctor,
                            CreateClinicUseCase createClinic,
                            UpdateDoctorLocationUseCase updateLocation,
                            GetDoctorsByLocationUseCase searchUseCase,
                            GetDoctorsByClinicUseCase clinicUseCase,
                            CreateScheduleUseCase createSchedule) {
        this.createDoctor = createDoctor;
        this.createClinic = createClinic;
        this.updateLocation = updateLocation;
        this.searchUseCase = searchUseCase;
        this.clinicUseCase = clinicUseCase;
        this.createSchedule = createSchedule;
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/cdoctor")
    public UUID createDoctor(@Body CreateDoctorRequest req) {
        if (req.getType() == null) throw new RuntimeException("Type cannot be null");
        return createDoctor.execute(req.getName(), req.getClinicId(),
                DoctorType.valueOf(String.valueOf(req.getType())), req.getSpecialization());
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/cclinic")
    public UUID createClinic(@Body CreateClinicRequest req) {
        return createClinic.execute(req.getName(), req.getLocationText());
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/{doctorId}/location")
    public void updateLocation(@PathVariable UUID doctorId, @Body LocationRequest request) {
        updateLocation.execute(doctorId, request.getLocationText());
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/searchnear")
    public List<Doctor> searchNear(@QueryValue String location) {
        return searchUseCase.executeNear(location);
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/search")
    public List<Doctor> search(@QueryValue String geohashPrefix) {
        return searchUseCase.execute(geohashPrefix);
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/clinic/{clinicId}")
    public List<Doctor> getByClinic(@PathVariable UUID clinicId) {
        return clinicUseCase.execute(clinicId);
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/schedule")
    public void createSchedule(@Body CreateScheduleRequest req) {
        createSchedule.execute(req.getDoctorId(), req.getWorkingDays(),
                req.getStartTime(), req.getEndTime(),
                req.getSlotDurationMinutes(), req.getMaxAppointmentsPerDay());
    }
}