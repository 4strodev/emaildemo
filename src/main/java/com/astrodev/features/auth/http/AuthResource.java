package com.astrodev.features.auth.http;

import com.astrodev.features.auth.application.AuthService;
import com.astrodev.features.auth.application.CreateSessionDTO;
import com.astrodev.shared.http.HttpErrorDetails;
import com.astrodev.shared.http.HttpResponse;
import com.astrodev.shared.monads.Err;
import com.astrodev.shared.monads.Ok;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/auth")
public class AuthResource {
    @Inject
    AuthService authService;

    @POST
    public HttpResponse createSession(CreateSessionDTO createSessionDTO) {
        return switch (this.authService.createSession(createSessionDTO)) {
            case Ok(var session) -> HttpResponse.success(session);
            case Err(var error) -> HttpResponse.error(new HttpErrorDetails(error.getMessage()));
        };
    }
}
