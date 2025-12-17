package com.astrodev.features.users.http;

import com.astrodev.features.users.application.CreateUserDTO;
import com.astrodev.features.users.application.UserService;
import com.astrodev.features.users.http.dtos.HTTPCreateUserDTO;
import com.astrodev.shared.http.HttpErrorDetails;
import com.astrodev.shared.http.HttpResponse;
import com.astrodev.shared.monads.Err;
import com.astrodev.shared.monads.Ok;
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
        var result = this.userService.save(new CreateUserDTO(id, body.name(), body.email(), body.password()));
        return switch (result) {
            case Ok(var nullValue) -> HttpResponse.success(nullValue);
            case Err(var error) -> HttpResponse.error(HttpErrorDetails.fromThrowable(error));
        };
    }

    @Path("/ping")
    @GET
    public HttpResponse ping() {
        return HttpResponse.success("pong");
    }
}
