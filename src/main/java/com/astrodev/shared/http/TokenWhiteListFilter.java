package com.astrodev.shared.http;

import com.astrodev.features.auth.application.AuthService;
import com.astrodev.shared.monads.Err;
import io.quarkus.security.UnauthorizedException;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import java.util.Objects;
import java.util.UUID;

public class TokenWhiteListFilter {
    Logger LOG = Logger.getLogger(TokenWhiteListFilter.class);

    @Inject
    JsonWebToken jwt;

    @Inject
    AuthService authService;

    @ServerRequestFilter
    public void tokenWhiteListCheck(ContainerRequestContext context) {
        // Not an authenticated endpoint
        if (jwt.getClaimNames() == null) {
            return;
        }

        var sessionId = UUID.fromString(jwt.getClaim("sid"));
        var result = this.authService.validateSession(sessionId);
        if (Objects.requireNonNull(result) instanceof Err(Exception e)) {
            throw new UnauthorizedException("Invalid token", e);
        }
    }
}
