package com.astrodev.features.product;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "product_stats")
public class ProductStats extends PanacheEntityBase {
    @Id
    @Column(name = "product_id")
    public UUID productId;

    @OneToOne
    @JoinColumn(name = "product_id",
            foreignKey = @ForeignKey(name = "product_stats_product_id_fkey"),
            nullable = false
    )
    public Product product;

    @Column(name = "count_orders")
    public BigInteger countOrders;

    @Column(name = "avg_price")
    public BigDecimal avgPrice;

    @Column(name = "max_price")
    public BigDecimal maxPrice;

    @Column(name = "min_price")
    public BigDecimal minPrice;

    @Column(name = "avg_amount")
    public BigDecimal avgAmount;

    @Column(name = "max_amount")
    public BigInteger maxAmount;

    @Column(name = "min_amount")
    public BigInteger minAmount;
}
