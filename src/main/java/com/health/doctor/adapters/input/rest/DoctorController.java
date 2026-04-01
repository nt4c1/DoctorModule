package com.health.doctor.adapters.input.rest;

import com.health.doctor.adapters.input.rest.dto.CreateClinicRequest;
import com.health.doctor.adapters.input.rest.dto.CreateDoctorRequest;
import com.health.doctor.adapters.input.rest.dto.LocationRequest;
import com.health.doctor.adapters.output.mapper.DoctorMapper;
import com.health.doctor.application.usecase.*;
import com.health.doctor.domain.model.DoctorType;
import com.health.doctor.domain.model.Doctor;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import javax.print.Doc;
import java.util.List;
import java.util.UUID;

@Controller("/doctor")
public class DoctorController {

    private final CreateDoctorUseCase createDoctor;
    private final CreateClinicUseCase createClinic;
    private final UpdateDoctorLocationUseCase updateLocation;
    private final GetDoctorsByLocationUseCase searchUseCase;
    private final GetDoctorsByClinicUseCase getDoctorsByClinicUseCase;

    public DoctorController(
            CreateDoctorUseCase createDoctor, CreateClinicUseCase createClinic, UpdateDoctorLocationUseCase updateLocation,
            GetDoctorsByLocationUseCase searchUseCase, GetDoctorsByClinicUseCase getDoctorsByClinicUseCase) {
        this.createDoctor = createDoctor;
        this.createClinic = createClinic;

        this.updateLocation = updateLocation;
        this.searchUseCase = searchUseCase;
        this.getDoctorsByClinicUseCase = getDoctorsByClinicUseCase;
    }

    // Create Doctor
    @Post("/cdoctor")
    public UUID createDoctor(@Body CreateDoctorRequest req){

        if (req.getType() == null) {
            throw new RuntimeException("Type cannot be null");
        }

        return createDoctor.execute(
                req.getName(),
                req.getClinicId(),
                DoctorType.valueOf(String.valueOf(req.getType())),
                req.getSpecialization()
        );
    }

    // Create Clinic
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/cclinic")
    public UUID createClinic(@Body CreateClinicRequest req){

        return createClinic.execute(
                req.getName(),
                req.getLocationText()
        );
    }

    // Update Location
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/{doctorId}/location")
    public void updateLocation(
            @PathVariable UUID doctorId,
            @Body LocationRequest request ){

        updateLocation.execute(doctorId, request.getLocationText());
    }

    //  Search Doctors
    @Get("/search")
    public Object search(@QueryValue String geohashPrefix) {
        return searchUseCase.execute(geohashPrefix)
                .stream()
                .map(DoctorMapper::toResponse)
                .toList();
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/searchnear")
    public Object searcher(@QueryValue String location) {
        return searchUseCase.executeNear(location)
                .stream()
                .map(DoctorMapper::toResponse)
                .toList();
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get("/clinic/{clinicId}")
    public List<Doctor> getByClinic(@PathVariable UUID clinicId) {
        return getDoctorsByClinicUseCase.execute(clinicId);
    }
}