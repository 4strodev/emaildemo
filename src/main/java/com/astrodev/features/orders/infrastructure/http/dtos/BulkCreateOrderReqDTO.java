package com.astrodev.features.orders.infrastructure.http.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record BulkCreateOrderReqDTO(
        @NotNull UUID orderId,
        @NotNull UUID productId,
        @Positive BigInteger amount,
        Optional<Instant> createdAt
) {
}
