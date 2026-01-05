package com.astrodev.features.auth;

import com.astrodev.features.users.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
public class AuthSession extends PanacheEntityBase {
    @Id
    public UUID id;
    @Column
    public Instant expirationTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    public AuthSession() {
    }
}
