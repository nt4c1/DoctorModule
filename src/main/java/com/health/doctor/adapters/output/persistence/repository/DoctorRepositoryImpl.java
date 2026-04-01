package com.health.doctor.adapters.output.persistence.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.health.doctor.domain.model.*;
import com.health.doctor.domain.ports.DoctorRepositoryPort;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;

import java.util.*;

@Serdeable
@Introspected
@Serdeable.Deserializable
@Singleton
public class DoctorRepositoryImpl implements DoctorRepositoryPort {

    private final CqlSession session;

    public DoctorRepositoryImpl(CqlSession session) {
        this.session = session;
    }

    @Override
    public void save(Doctor d) {

        session.execute(
                "INSERT INTO doctor_service.doctors (doctor_id, name, clinic_id, type, specialization, is_active) VALUES (?,?,?,?,?,?)",
                d.getId(),
                d.getName(),
                d.getClinicId(),
                d.getType().name(),
                d.getSpecialization(),
                d.isActive()
        );
    }

    @Override
    public void updateLocation(UUID doctorId, Location location) {

        String prefix = location.getGeohash().substring(0, 6);

        session.execute(
                "INSERT INTO doctor_service.doctors_by_location (geohash_prefix, doctor_id, latitude, longitude, location_text) VALUES (?,?,?,?,?)",
                prefix,
                doctorId,
                location.getLatitude(),
                location.getLongitude(),
                location.getLocationText()
        );
    }

    @Override
    public Doctor findById(UUID doctorId) {

        Row r = session.execute(
                "SELECT * FROM doctor_service.doctors WHERE doctor_id=?",
                doctorId
        ).one();

        if (r == null) return null;

        return new Doctor(
                r.getUuid("doctor_id"),
                r.getString("name"),
                r.getUuid("clinic_id"),
                DoctorType.valueOf(r.getString("type")),
                r.getString("specialization"),
                r.getBoolean("is_active")
        );
    }

    @Override
    public List<Doctor> findByGeohashPrefix(String prefix) {

        ResultSet rs = session.execute(
                "SELECT * FROM doctor_service.doctors_by_location WHERE geohash_prefix=?",
                prefix
        );

        List<Doctor> list = new ArrayList<>();

        for (Row r : rs) {
            list.add(new Doctor(
                    r.getUuid("doctor_id"),
                    null,
                    null,
                    null,
                    null,
                    true
            ));
        }

        return list;
    }
}