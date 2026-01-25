package com.astrodev.shared.events;

import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EventBus {
    @Channel("domain-events")
    Emitter<Event<?>> domainEventEmitter;

    public void publish(Event<?> event) {
        Logger.getLogger(EventBus.class).info("Publishing event");
        var metadata = OutgoingRabbitMQMetadata.builder().withRoutingKey(event.eventType).build();
        Message<Event<?>> message = Message.of(event);
        this.domainEventEmitter.send(
                message.addMetadata(metadata)
        );
    }
}
