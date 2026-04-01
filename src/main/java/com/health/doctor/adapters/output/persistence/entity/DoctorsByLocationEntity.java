package com.health.doctor.adapters.output.persistence.entity;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.Setter;
import com.datastax.oss.driver.api.mapper.annotations.*;


import java.util.UUID;

@Getter
@Setter
@Serdeable
@Serdeable.Deserializable
@CqlName("doctors_by_location")
public class DoctorsByLocationEntity {
    @PartitionKey
    private String geohashPrefix;

    @ClusteringColumn
    private UUID doctorId;

    private double latitude;
    private double longitude;
    private String locationText;
}
