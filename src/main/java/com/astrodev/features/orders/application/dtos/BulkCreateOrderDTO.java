package com.astrodev.features.orders.application.dtos;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record BulkCreateOrderDTO(UUID orderId, UUID productId, BigInteger amount, Optional<Instant> timestamp) {
}
