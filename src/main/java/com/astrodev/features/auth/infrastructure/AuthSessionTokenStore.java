package com.astrodev.features.auth.infrastructure;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class AuthSessionTokenStore {
    Map<UUID, String> sessionMap = new HashMap<>();

    public boolean exists(UUID sessionId) {
        return this.sessionMap.containsKey(sessionId);
    }

    public void save(UUID id, String refreshToken) {
        this.sessionMap.put(id, refreshToken);
    }

    public void remove(UUID sessionId) {
        this.sessionMap.remove(sessionId);
    }
}
