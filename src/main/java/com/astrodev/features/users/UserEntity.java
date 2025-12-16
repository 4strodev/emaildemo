package com.astrodev.features.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {
    public UserEntity() {}

    @Id
    UUID id;

    @Column(length = 255)
    String email;

    @Column(length = 255)

    String password;
}
