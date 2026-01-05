package com.astrodev.features.auth.infrastructure.http.dtos;

public record SessionCreateResDTO(String refreshToken, String accessToken) {
}
