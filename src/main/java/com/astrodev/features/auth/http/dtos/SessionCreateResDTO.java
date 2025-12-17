package com.astrodev.features.auth.http.dtos;

public record SessionCreateResDTO(String refreshToken, String accessToken) {
}
