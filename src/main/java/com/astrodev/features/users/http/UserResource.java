package com.astrodev.features.users.http;

import com.astrodev.features.users.application.CreateUserDTO;
import com.astrodev.features.users.application.UserService;
import com.astrodev.features.users.http.dtos.HTTPCreateUserDTO;
import com.astrodev.shared.http.HttpResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.UUID;

@Path("/user")
public class UserResource {
    @Inject
    UserService userService;

    @Path("{id}")
    @PUT
    @Operation(summary = "Creates a new user in the platform")
    @PermitAll
    public HttpResponse saveUser(@PathParam("id") UUID id, HTTPCreateUserDTO body) {
        this.userService.create(new CreateUserDTO(id, body.name(), body.email(), body.password()));
        return HttpResponse.success(null);
    }

    @Path("/ping")
    @GET
    public HttpResponse ping() {
        return HttpResponse.success("pong");
    }
}
