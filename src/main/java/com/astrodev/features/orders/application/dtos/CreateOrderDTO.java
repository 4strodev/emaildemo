package com.astrodev.features.orders.application.dtos;

import java.math.BigInteger;
import java.util.UUID;

public record CreateOrderDTO(UUID orderId, UUID customerId, UUID productId, BigInteger amount) {
}
