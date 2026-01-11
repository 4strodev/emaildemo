package com.astrodev.features.healthcheck.infrastructure.http;

import com.astrodev.shared.http.response.HttpResponse;
import io.quarkus.security.Authenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/healthcheck")
public class HealthCheckResource {
    @Path("/ping")
    @GET
    @Authenticated
    public HttpResponse ping() {
        return HttpResponse.success("pong");
    }
}
