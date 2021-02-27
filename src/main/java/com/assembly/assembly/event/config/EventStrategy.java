package com.assembly.assembly.event.config;

import com.assembly.assembly.event.payload.Payload;

public interface EventStrategy {

    <T extends Payload,E extends EventType> DomainEvent newInstance(T payload);
}
