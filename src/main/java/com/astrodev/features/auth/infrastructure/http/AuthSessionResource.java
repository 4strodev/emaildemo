package com.astrodev.features.auth.infrastructure.http;

import com.astrodev.features.auth.application.AuthService;
import com.astrodev.features.auth.application.dtos.CreateSessionDTO;
import com.astrodev.features.auth.application.dtos.DeleteSessionDTO;
import com.astrodev.features.auth.application.dtos.RefreshSessionDTO;
import com.astrodev.features.auth.infrastructure.AuthSessionTokenStore;
import com.astrodev.features.auth.infrastructure.http.dtos.SessionCreateResDTO;
import com.astrodev.features.auth.infrastructure.http.dtos.SessionDeleteReqDTO;
import com.astrodev.features.auth.infrastructure.http.dtos.SessionRefreshReqDTO;
import com.astrodev.features.auth.infrastructure.http.dtos.SessionRefreshResDTO;
import com.astrodev.shared.http.response.HttpErrorDetails;
import com.astrodev.shared.http.response.HttpErrorResponseData;
import com.astrodev.shared.http.response.HttpResponse;
import com.astrodev.shared.monads.Err;
import com.astrodev.shared.monads.Ok;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/auth/session")
public class AuthSessionResource {
    @Inject
    AuthService authService;

    @Inject
    AuthSessionTokenStore tokenStore;

    @POST
    @PermitAll
    public HttpResponse createSession(CreateSessionDTO createSessionDTO) throws Exception {
        final var result = this.authService.createSession(createSessionDTO);
        return switch (result) {
            case Err(var error) -> throw error;
            case Ok(var tokens) -> HttpResponse.success(new SessionCreateResDTO(
                    tokens.refreshToken(),
                    tokens.accessToken()
            ));
        };
    }

    @PATCH
    @PermitAll
    public HttpResponse refreshSession(SessionRefreshReqDTO sessionRefreshReqDTO) {
        var result = this.authService.refreshSession(new RefreshSessionDTO(sessionRefreshReqDTO.refreshToken()));
        return switch (result) {
            case Err(var error) -> HttpResponse.error(new HttpErrorResponseData(
                    null,
                    HttpErrorDetails.fromThrowable(error)
            ));
            case Ok(var tokens) -> HttpResponse.success(new SessionRefreshResDTO(tokens.accessToken()));
        };
    }

    @DELETE
    @PermitAll
    public HttpResponse deleteSession(SessionDeleteReqDTO sessionDeleteReqDTO) {
        var result = this.authService.deleteSession(new DeleteSessionDTO(sessionDeleteReqDTO.refreshToken()));
        return switch (result) {
            case Err(var error) -> HttpResponse.error(new HttpErrorResponseData(
                    null,
                    HttpErrorDetails.fromThrowable(error)
            ));
            case Ok(var _) -> HttpResponse.success(null);
        };
    }
}
