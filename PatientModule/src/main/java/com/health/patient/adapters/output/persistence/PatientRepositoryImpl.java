package com.health.patient.adapters.output.persistence;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.health.patient.domain.model.Patient;
import com.health.patient.domain.ports.PatientRepositoryPort;
import jakarta.inject.Singleton;
import java.util.UUID;

@Singleton
public class PatientRepositoryImpl implements PatientRepositoryPort {

    private final CqlSession session;

    public PatientRepositoryImpl(CqlSession session) {
        this.session = session;
    }

    @Override
    public void save(Patient patient) {
        session.execute(
                "INSERT INTO doctor_service.patients (patient_id, name) VALUES (?,?)",
                patient.getId(), patient.getName()
        );
    }

    @Override
    public Patient findById(UUID patientId) {
        Row r = session.execute(
                "SELECT * FROM doctor_service.patients WHERE patient_id=?",
                patientId
        ).one();
        if (r == null) return null;
        return new Patient(r.getUuid("patient_id"), r.getString("name"));
    }
}