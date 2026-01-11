package com.astrodev.features.orders.application;

import com.astrodev.features.orders.Order;
import com.astrodev.features.orders.application.dtos.CreateOrderDTO;
import com.astrodev.features.orders.infrastructure.OrderRepository;
import com.astrodev.features.product.infrastructure.ProductRepository;
import com.astrodev.features.users.infrastructure.UserRepository;
import com.astrodev.shared.monads.Result;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@ApplicationScoped
public class OrderService {
    @Inject
    OrderRepository orderRepository;

    @Inject
    ProductRepository productRepository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public Result<Void, Throwable> createOrder(CreateOrderDTO createOrderDTO) {
        var user = this.userRepository.find("id", createOrderDTO.customerId()).firstResult();
        if (user == null) {
            return Result.err(new Exception("User not found"));
        }
        var product = this.productRepository.find("id", createOrderDTO.productId()).firstResult();
        if (product == null) {
            return Result.err(new Exception("Product not found"));
        }

        try {
            var order = new Order();
            order.orderId = createOrderDTO.orderId();
            order.user = user;
            order.createdAt = Instant.now();
            order.totalPrice = product.price.multiply(new BigDecimal(createOrderDTO.amount()));
            order.productId = product.id;
            order.amount = createOrderDTO.amount();
            this.orderRepository.persist(order);

            return Result.ok(null);
        } catch (Exception e) {
            return Result.err(e);
        }
    }
}
