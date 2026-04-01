package com.health.doctor.adapters.output.persistence.entity;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Serdeable
@Serdeable.Deserializable
@CqlName("doctors")
public class DoctorEntity {

    @PartitionKey
    private UUID doctorId;

    private String name;
    private UUID clinicId;
    private String type;
    private String specialization;
    private boolean isActive;
}