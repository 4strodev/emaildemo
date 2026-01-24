package com.astrodev.features.orders;

import com.astrodev.features.users.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order extends PanacheEntityBase {
    @Column(name = "product_id")
    public UUID productId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;
    @Column(name = "amount")
    public BigInteger amount;
    @Column(name = "total_price")
    public BigDecimal totalPrice;
    @EmbeddedId
    public OrderId orderId;
}
