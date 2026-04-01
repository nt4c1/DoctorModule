package com.health.doctor.adapters.output.mapper;


import com.health.doctor.adapters.input.rest.dto.DoctorResponse;
import com.health.doctor.domain.model.Doctor;

public class DoctorMapper {

    public static DoctorResponse toResponse(Doctor doctor)
    {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getClinicId()
        );
    }
}
