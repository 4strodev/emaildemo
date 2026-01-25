package com.astrodev.features.orders.application.events;

import com.astrodev.features.orders.application.dtos.OrderDto;
import com.astrodev.shared.events.Event;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderCreatedEvent extends Event<OrderDto> {
    public OrderCreatedEvent(@JsonProperty("attributes") OrderDto orderDto) {
        super("order.created", orderDto);
    }
}
