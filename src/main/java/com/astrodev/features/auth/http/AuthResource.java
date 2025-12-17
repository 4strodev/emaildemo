package com.astrodev.features.auth.http;

import com.astrodev.features.auth.application.AuthService;
import com.astrodev.features.auth.application.AuthSessionTokenStore;
import com.astrodev.features.auth.application.CreateSessionDTO;
import com.astrodev.features.auth.http.dtos.SessionCreateResDTO;
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

    @Inject
    AuthSessionTokenStore tokenStore;

    @POST
    public HttpResponse createSession(CreateSessionDTO createSessionDTO) {
        final var result = this.authService.createSession(createSessionDTO);
        return switch (result) {
            case Err(var error) -> HttpResponse.error(HttpErrorDetails.fromThrowable(error));
            case Ok(var tokens) -> HttpResponse.success(new SessionCreateResDTO(
                    tokens.refreshToken(),
                    tokens.accessToken()
            ));
        };
    }
}
