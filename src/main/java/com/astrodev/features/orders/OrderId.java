package com.astrodev.features.orders;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Embeddable
public record OrderId(
        @Column(name = "order_id")
        UUID orderId,

        @Column(name = "created_at")
        Instant createdAt
) implements Serializable {
}
