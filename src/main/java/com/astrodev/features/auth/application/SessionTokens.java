package com.astrodev.features.auth.application;

public record SessionTokens(String refreshToken, String accessToken) {
}
