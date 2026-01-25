package com.astrodev.features.orders.application.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

@RegisterForReflection
public record OrderDto(
        UUID orderId,
        UUID productId,
        Instant createdAt,
        UUID userId,
        BigInteger amount,
        BigDecimal totalPrice
) {
}
