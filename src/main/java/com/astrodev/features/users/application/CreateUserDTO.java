package com.astrodev.features.users.application;

import java.util.UUID;

public record CreateUserDTO(UUID id, String username, String email, String password) {
}
