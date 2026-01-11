package com.astrodev.features.product.infrastructure.http.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SaveProductReqDTO(@NotNull String productType, @Positive BigDecimal price) {
}
