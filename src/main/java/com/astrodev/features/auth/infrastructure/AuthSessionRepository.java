package com.astrodev.features.auth.infrastructure;

import com.astrodev.features.auth.AuthSession;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthSessionRepository implements PanacheRepository<AuthSession> {
}
