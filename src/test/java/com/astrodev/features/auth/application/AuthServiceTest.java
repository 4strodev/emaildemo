package com.astrodev.features.auth.application;

import com.astrodev.features.auth.AuthSession;
import com.astrodev.features.auth.infrastructure.AuthSessionRepository;
import com.astrodev.features.users.User;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@QuarkusTest
public class AuthServiceTest {
    @Inject
    AuthService authService;

    @InjectMock
    AuthSessionRepository authSessionRepository;

    @Test
    void refreshTokenUsingExpiredToken() {
        doNothing().when(authSessionRepository).persist(Collections.singleton(any()));

        User user = new User();
        user.username = "mock user";
        user.id = UUID.randomUUID();
        user.email = "mockuser@example.com";

        var expiredSession = new AuthSession();
        expiredSession.expirationTime = Instant.now().minus(1, ChronoUnit.HOURS);
        expiredSession.id = UUID.randomUUID();
        expiredSession.user = user;

        final var refreshToken = Jwt
                .issuer("email-demo")
                .subject(expiredSession.user.id.toString())
                .expiresAt(expiredSession.expirationTime)
                .claim("sid", expiredSession.id.toString())
                .claim("jti", UUID.randomUUID()).sign();

        var result = authService.refreshSession(new RefreshSessionDTO(refreshToken));
        Assertions.assertTrue(result.isErr(), "refreshSession result");
    }
}
