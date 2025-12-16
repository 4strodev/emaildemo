package com.astrodev.features.users;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.UUID;

@Path("/user")
public class UserResource {
    @Path("{id}")
    @POST
    @Operation(summary = "Creates a new user in the platform")
    public void saveUser(@PathParam("id") UUID id, CreateUserDTO createUserDTO) {
    }
}
