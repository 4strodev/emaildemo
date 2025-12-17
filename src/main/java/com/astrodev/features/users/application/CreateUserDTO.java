package com.astrodev.features.users.application;

import java.util.UUID;

public record CreateUserDTO(UUID id, String name, String email, String password) {
}
