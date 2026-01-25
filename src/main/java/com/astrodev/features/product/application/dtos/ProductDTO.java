package com.astrodev.features.product.application.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.util.UUID;

@RegisterForReflection
public record ProductDTO(
        UUID id,
        String name,
        BigDecimal price
) {
}
