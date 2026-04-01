package com.health.doctor.adapters.output.mapper;

import com.health.doctor.adapters.input.rest.dto.PatientResponse;
import com.health.doctor.domain.model.Patient;

public class PatientMapper {

    public static PatientResponse toResponse(Patient patient){
        return new PatientResponse(
                patient.getId(),
                patient.getName()
        );
    }
}
