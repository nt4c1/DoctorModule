package com.health.doctor.adapters.output.persistence.entity;


import com.datastax.oss.driver.api.mapper.annotations.*;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Serdeable
@Serdeable.Deserializable
@CqlName("appointments_by_doctor")
public class AppointmentByDoctorEntity {
    @PartitionKey
    private UUID doctorId;

    @PartitionKey
    private LocalDate appointmentDate;

    @ClusteringColumn
    private LocalTime scheduledTime;

    @ClusteringColumn(1)
    private UUID appointmentId;

    private UUID patientId;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
