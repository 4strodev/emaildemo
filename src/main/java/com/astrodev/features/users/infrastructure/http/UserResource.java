package com.astrodev.features.users.infrastructure.http;

import com.astrodev.features.users.application.CreateUserDTO;
import com.astrodev.features.users.application.UserService;
import com.astrodev.features.users.infrastructure.http.dtos.HTTPCreateUserDTO;
import com.astrodev.shared.http.HttpResponse;
import com.astrodev.shared.monads.Err;
import com.astrodev.shared.monads.Ok;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
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
    public Response saveUser(@PathParam("id") UUID id, @Valid HTTPCreateUserDTO body) throws Throwable {
        var result = this.userService.save(new CreateUserDTO(id, body.username(), body.email(), body.password()));
        return switch (result) {
            case Ok(var _) -> Response.status(Response.Status.CREATED).entity(HttpResponse.success(null)).build();
            case Err(var error) -> throw error;
        };
    }


}
