package com.astrodev.features.product.application;

import com.astrodev.features.product.Product;
import com.astrodev.features.product.ProductStats;
import com.astrodev.features.product.application.dtos.ProductDTO;
import com.astrodev.features.product.application.dtos.ProductStatsDTO;
import com.astrodev.features.product.application.dtos.SaveProductDTO;
import com.astrodev.features.product.infrastructure.ProductRepository;
import com.astrodev.features.product.infrastructure.ProductStatsRepository;
import com.astrodev.shared.monads.Result;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;

    @Inject
    ProductStatsRepository productStatsRepository;


    @Transactional
    public Result<Void, Throwable> saveProduct(SaveProductDTO productDTO) {
        return Result.fromSupplier(() -> {
            var product = new Product();
            product.id = productDTO.id();
            product.price = productDTO.price();
            product.name = productDTO.productType();

            this.productRepository.getEntityManager().merge(product);
            return null;
        });
    }

    public Result<List<ProductStatsDTO>, Throwable> getProductStats() {
        try {
            var stats = this.productStatsRepository
                    .findAll()
                    .stream()
                    .map(this::productStatsMapper)
                    .toList();
            return Result.ok(stats);
        } catch (Exception e) {
            return Result.err(e);
        }
    }

    public Result<Optional<ProductStatsDTO>, Throwable> findProductStats(UUID productId) {
        try {
            var stats = this.productStatsRepository
                    .find("id", productId)
                    .firstResultOptional()
                    .map(this::productStatsMapper);
            return Result.ok(stats);
        } catch (Exception e) {
            return Result.err(e);
        }
    }

    ProductStatsDTO productStatsMapper(ProductStats productStats) {
        return new ProductStatsDTO(
                new ProductDTO(
                        productStats.productId,
                        productStats.product.name,
                        productStats.product.price
                ),
                productStats.countOrders,
                productStats.maxPrice,
                productStats.minPrice,
                productStats.avgPrice,
                productStats.maxAmount,
                productStats.minAmount,
                productStats.avgAmount
        );
    }
}
