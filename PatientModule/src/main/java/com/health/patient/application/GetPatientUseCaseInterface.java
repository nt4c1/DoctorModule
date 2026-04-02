package com.health.patient.application;

import com.health.patient.domain.model.Patient;

import java.util.UUID;

public interface GetPatientUseCaseInterface {
    Patient execute(UUID id);
}
