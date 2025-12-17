package com.astrodev.features.users;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "email"
        }
))
public class User {
    @Id
    public UUID id;
    @Column(length = 255)
    public String username;
    @Column(length = 255)
    public String email;
    @Column(length = 255)
    public String password;

    public User() {
    }
}
