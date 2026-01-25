package com.astrodev.features.orders.application;

import com.astrodev.features.orders.Order;
import com.astrodev.features.orders.OrderId;
import com.astrodev.features.orders.application.dtos.BulkCreateOrderDTO;
import com.astrodev.features.orders.application.dtos.CreateOrderDTO;
import com.astrodev.features.orders.infrastructure.OrderRepository;
import com.astrodev.features.product.Product;
import com.astrodev.features.product.infrastructure.ProductRepository;
import com.astrodev.features.users.infrastructure.UserRepository;
import com.astrodev.shared.monads.Result;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
            order.orderId = new OrderId(
                    createOrderDTO.orderId(),
                    Instant.now()
            );
            order.user = user;
            order.totalPrice = product.price.multiply(new BigDecimal(createOrderDTO.amount()));
            order.productId = product.id;
            order.amount = createOrderDTO.amount();
            this.orderRepository.persist(order);

            return Result.ok(null);
        } catch (Exception e) {
            return Result.err(e);
        }
    }

    @Transactional
    public Result<Void, Throwable> createBulkOrder(List<BulkCreateOrderDTO> createOrdersDto, UUID customerId) {
        var user = this.userRepository.find("id", customerId).firstResult();
        if (user == null) {
            return Result.err(new Exception("User not found"));
        }

        var productIds =
                createOrdersDto.stream().parallel().map(BulkCreateOrderDTO::productId).collect(Collectors.toSet());

        var products = this.productRepository.find("id IN ?1", productIds).list();
        if (products.size() != productIds.size()) {
            return Result.err(new Exception("Product not found"));
        }

        var productMap = new HashMap<UUID, Product>();
        for (var product : products) {
            productMap.put(product.id, product);
        }

        try {
            var orders = createOrdersDto.stream().map(dto -> {
                var product = productMap.get(dto.productId());
                var order = new Order();
                order.orderId = new OrderId(
                        dto.orderId(),
                        dto.timestamp().orElse(Instant.now())
                );
                order.user = user;
                order.totalPrice = product.price.multiply(new BigDecimal(dto.amount()));
                order.productId = product.id;
                order.amount = dto.amount();
                this.orderRepository.persist(order);

                return order;
            }).toList();
            this.orderRepository.persist(orders);
            return Result.ok(null);
        } catch (Exception e) {
            return Result.err(e);
        }

    }
}
