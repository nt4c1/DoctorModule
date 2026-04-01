package com.health.doctor.adapters.output.client;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("${photon.base-url}")
public interface PhotonClient {
    @Get("?q={query}&limit=${photon.search-limit}")
    PhotonResponse search(String query);
}