package com.astrodev.features.users.http.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record HTTPCreateUserDTO(@NotBlank @Length(max = 100) String name, @Email String email,
                                @NotBlank String password) {
}
