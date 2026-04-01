package com.health.doctor.adapters.output.persistence.entity;

import com.datastax.oss.driver.api.mapper.annotations.*;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@CqlName("clinics")
public class ClinicEntity {
    @PartitionKey
    private UUID clinicId;

    private String name;
    private double latitude;
    private double longitude;
    private String geohash;
    private String locationText;
    private boolean isActive;
}
