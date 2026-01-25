package com.astrodev.features.product.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(
        UUID id,
        String name,
        BigDecimal price
) {
}
