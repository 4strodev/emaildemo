package com.astrodev.features.users;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.UUID;

@Path("/user")
public class UserResource {
    @Inject
    UserService userService;

    @Path("{id}")
    @POST
    @Operation(summary = "Creates a new user in the platform")
    public void saveUser(@PathParam("id") UUID id, CreateUserDTO createUserDTO) {
        this.userService.create(id, createUserDTO);
        this.userService.sendEmail(createUserDTO.email(), "User created successfully");
    }

    @Path("email")
    @POST
    public void sendEmail(SendEmailDTO sendEmailDTO) {
        this.userService.sendEmail(sendEmailDTO.to(), sendEmailDTO.message());
    }
}
