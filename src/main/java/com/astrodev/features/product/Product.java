package com.astrodev.features.product;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Product extends PanacheEntityBase {
    @Id
    public UUID id;

    @Column(name = "name")
    public String name;

    @Column(name = "price")
    public BigDecimal price;
}
