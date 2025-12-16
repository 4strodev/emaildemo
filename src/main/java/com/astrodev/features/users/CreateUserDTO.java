package com.astrodev.features.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(
        @Email
        String email,
        @NotBlank
        String password) {
}
