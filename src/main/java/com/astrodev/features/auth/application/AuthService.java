package com.astrodev.features.auth.application;

import com.astrodev.features.auth.AuthSession;
import com.astrodev.features.users.User;
import com.astrodev.shared.monads.Result;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@ApplicationScoped
public class AuthService {
    @Inject
    EntityManager entityManager;
    @Inject
    AuthSessionTokenStore authSessionTokenStore;
    @Inject
    JWTParser jwtParser;

    Logger LOG = Logger.getLogger(AuthService.class);

    @Transactional
    public Result<SessionTokens, Exception> createSession(CreateSessionDTO createSessionDTO) {
        var user = this.entityManager.createQuery("Select u from User u where u.email = :email", User.class)
                .setParameter("email", createSessionDTO.email())
                .getSingleResultOrNull();

        if (user == null) {
            return Result.err(new Exception("User not found"));
        }

        var session = new AuthSession();

        session.id = UUID.randomUUID();
        session.user = user;
        session.expirationTime = Instant.now().plus(5, ChronoUnit.HOURS);

        this.entityManager.persist(session);
        var tokens = this.createTokens(session);

        this.authSessionTokenStore.save(session.id, tokens.refreshToken());

        return Result.ok(tokens);
    }

    public Result<SessionTokens, Exception> refreshSession(RefreshSessionDTO refreshSessionDTO) {
        JsonWebToken jwt;
        try {
            jwt = jwtParser.parse(refreshSessionDTO.refreshToken());
        } catch (ParseException exception) {
            return Result.err(exception);
        }

        UUID sessionId = UUID.fromString(jwt.getClaim("sid"));
        if (!this.authSessionTokenStore.exists(sessionId)) {
            return Result.err(new Exception("Refresh token revoked"));
        }

        AuthSession sessionFound = this.entityManager.find(AuthSession.class, sessionId);
        if (sessionFound == null) {
            return Result.err(new Exception("Session not found"));
        }

        var accessToken = this.createAccessToken(sessionFound);

        return Result.ok(new SessionTokens(refreshSessionDTO.refreshToken(), accessToken));
    }

    private SessionTokens createTokens(AuthSession authSession) {
        var refreshToken = this.createRefreshToken(authSession);
        var accessToken = this.createAccessToken(authSession);

        return new SessionTokens(refreshToken, accessToken);
    }

    private String createRefreshToken(AuthSession authSession) {
        return Jwt
                .issuer("email-demo")
                .subject(authSession.user.id.toString())
                .expiresAt(authSession.expirationTime)
                .claim("sid", authSession.id.toString())
                .claim("jti", UUID.randomUUID()).sign();
    }

    private String createAccessToken(AuthSession authSession) {
        return Jwt
                .issuer("email-demo")
                .subject(authSession.user.id.toString())
                .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .claim("sid", authSession.id.toString())
                .claim("jti", UUID.randomUUID()).sign();
    }
}
