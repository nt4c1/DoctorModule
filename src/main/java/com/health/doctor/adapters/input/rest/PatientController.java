//TEMPORARY HO YO PATIENT

package com.health.doctor.adapters.input.rest;

import com.health.doctor.adapters.input.rest.dto.CreatePatientRequest;
import com.health.doctor.application.usecase.CreatePatientUseCase;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/patient")
public class PatientController {

    private final CreatePatientUseCase createPatient;

    public PatientController(CreatePatientUseCase createPatient) {
        this.createPatient = createPatient;
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post
    public Object create(@Body CreatePatientRequest request) {
        return createPatient.execute(request.getName());
    }
}