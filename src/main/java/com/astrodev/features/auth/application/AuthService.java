package com.astrodev.features.auth.application;

import com.astrodev.features.auth.AuthSession;
import com.astrodev.features.users.User;
import com.astrodev.shared.monads.Result;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.Session;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@ApplicationScoped
public class AuthService {
    @Inject
    Session hibernateSession;

    @Transactional
    public Result<AuthSession, Exception> createSession(CreateSessionDTO createSessionDTO) {
        var users = this.hibernateSession.createSelectionQuery("from User where email = :email", User.class)
                .setParameter("email", createSessionDTO.email())
                .getResultList();

        if (users.isEmpty()) {
            return Result.err(new Exception("User not found"));
        }

        var user = users.getFirst();

        var session = new AuthSession();

        session.id = UUID.randomUUID();
        session.user = user;
        session.expirationTime = Instant.now().plus(5, ChronoUnit.HOURS);

        this.hibernateSession.persist(session);
        return Result.ok(session);
    }
}
