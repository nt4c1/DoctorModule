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
@Entity
@Serdeable
@Serdeable.Deserializable
@CqlName("appointments_by_doctor_status")
public class AppointmentByDoctorStatusEntity {

    @PartitionKey(0)
    private UUID doctorId;

    @PartitionKey(1)
    private String status;

    @PartitionKey(2)
    private LocalDate appointmentDate;

    @ClusteringColumn(0)
    private LocalTime scheduledTime;

    private UUID appointmentId;
    private UUID patientId;
}