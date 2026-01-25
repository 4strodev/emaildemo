package com.astrodev.shared.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@RegisterForReflection
public class Event<T> implements Serializable {
    public UUID id;
    public Instant timestamp;
    public String eventType;
    public T attributes;

    public Event(@JsonProperty("eventType") String type, @JsonProperty("attributes") T attributes) {
        this.id = UUID.randomUUID();
        this.eventType = type;
        this.timestamp = Instant.now();
        this.attributes = attributes;
    }
}
