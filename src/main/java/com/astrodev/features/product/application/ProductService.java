package com.astrodev.features.product.application;

import com.astrodev.features.orders.application.events.OrderCreatedEvent;
import com.astrodev.features.product.Product;
import com.astrodev.features.product.ProductStats;
import com.astrodev.features.product.application.dtos.ProductDTO;
import com.astrodev.features.product.application.dtos.ProductStatsDTO;
import com.astrodev.features.product.application.dtos.SaveProductDTO;
import com.astrodev.features.product.infrastructure.ProductRepository;
import com.astrodev.features.product.infrastructure.ProductStatsRepository;
import com.astrodev.shared.monads.Result;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
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

    @Incoming("stats-builder")
    @Transactional
    public void createStat(JsonObject object) {
        OrderCreatedEvent event = object.mapTo(OrderCreatedEvent.class);
        var result = this.productStatsRepository.find("id", event.attributes.productId()).firstResultOptional();
        var calculatedProductStats = result
                .map(productStats -> {
                    productStats.countOrders = productStats.countOrders.add(BigInteger.ONE);
                    productStats.minAmount = productStats.minAmount.min(event.attributes.amount());
                    productStats.maxAmount = productStats.maxAmount.max(event.attributes.amount());
                    productStats.avgAmount = this.getAvg(
                            productStats.countOrders,
                            productStats.avgAmount,
                            new BigDecimal(event.attributes.amount())
                    );
                    productStats.minPrice = productStats.minPrice.min(event.attributes.totalPrice());
                    productStats.maxPrice = productStats.maxPrice.max(event.attributes.totalPrice());
                    productStats.avgPrice = this.getAvg(
                            productStats.countOrders,
                            productStats.avgPrice,
                            event.attributes.totalPrice()
                    );

                    return productStats;
                })
                .orElseGet(() -> {
                    var newProductStats = new ProductStats();
                    newProductStats.productId = event.attributes.productId();
                    newProductStats.countOrders = BigInteger.ONE;

                    newProductStats.avgAmount = new BigDecimal(event.attributes.amount());
                    newProductStats.minAmount = event.attributes.amount();
                    newProductStats.maxAmount = event.attributes.amount();

                    newProductStats.avgPrice = event.attributes.totalPrice();
                    newProductStats.minPrice = event.attributes.totalPrice();
                    newProductStats.maxPrice = event.attributes.totalPrice();

                    return newProductStats;
                });

        this.productStatsRepository.persist(calculatedProductStats);
    }

    public BigDecimal getAvg(BigInteger count, BigDecimal avg, BigDecimal newValue) {
        var newAvg = avg.add(newValue.subtract(avg).divide(
                new BigDecimal(count),
                MathContext.DECIMAL128
        ));

        Logger.getLogger("avg-logger").infov(
                "AVG params: {1} + (({2} - {1}) / {0}) = {3}", count, avg, newValue,
                newAvg
        );
        return newAvg;
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
