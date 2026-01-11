package com.astrodev.features.product.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record SaveProductDTO(UUID id, String productType, BigDecimal price) {
}
