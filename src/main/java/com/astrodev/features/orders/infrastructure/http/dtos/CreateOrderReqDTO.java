package com.astrodev.features.orders.infrastructure.http.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigInteger;
import java.util.UUID;

public record CreateOrderReqDTO(@NotNull UUID productId, @Positive BigInteger amount) {
}
