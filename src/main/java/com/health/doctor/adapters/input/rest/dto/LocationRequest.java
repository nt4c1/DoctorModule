package com.health.doctor.adapters.input.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Serdeable
@Getter
@Setter
@NoArgsConstructor
public class LocationRequest {

    private double latitude;
    private double longitude;
    private String geohash;
    private String locationText;

}