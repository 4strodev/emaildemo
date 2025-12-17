package com.astrodev.features.auth.application;

import com.astrodev.features.auth.AuthSession;
import com.astrodev.features.users.User;
import com.astrodev.shared.monads.Result;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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

    Logger LOG = Logger.getLogger(AuthService.class);

    @Transactional
    public Result<SessionTokens, Exception> createSession(CreateSessionDTO createSessionDTO) {
        LOG.info(createSessionDTO);
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

    private SessionTokens createTokens(AuthSession authSession) {
        var refreshToken = Jwt
                .issuer("email-demo")
                .subject(authSession.user.id.toString())
                .expiresAt(authSession.expirationTime)
                .claim("sid", authSession.id.toString())
                .claim("jti", UUID.randomUUID()).sign();

        var accessToken = Jwt
                .issuer("email-demo")
                .subject(authSession.user.id.toString())
                .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .claim("sid", authSession.id.toString())
                .claim("jti", UUID.randomUUID()).sign();

        return new SessionTokens(refreshToken, accessToken);
    }
}
