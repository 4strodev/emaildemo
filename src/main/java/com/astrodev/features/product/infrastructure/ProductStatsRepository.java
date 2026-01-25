package com.astrodev.features.product.infrastructure;

import com.astrodev.features.product.ProductStats;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductStatsRepository implements PanacheRepository<ProductStats> {
}
