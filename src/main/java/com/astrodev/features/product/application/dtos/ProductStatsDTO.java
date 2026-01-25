package com.astrodev.features.product.application.dtos;

import java.math.BigDecimal;
import java.math.BigInteger;

public record ProductStatsDTO(
        ProductDTO product,
        BigInteger totalOrders,

        BigDecimal maxPrice,
        BigDecimal minPrice,
        BigDecimal avgPrice,

        BigInteger maxAmount,
        BigInteger minAmount,
        BigDecimal avgAmount
) {
}
