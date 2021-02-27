package com.assembly.assembly.event.strategy;

import com.assembly.assembly.event.config.DomainEvent;
import com.assembly.assembly.event.config.EventStrategy;
import com.assembly.assembly.event.config.EventType;
import com.assembly.assembly.event.payload.Payload;

public enum TopicResultEvent implements EventType, EventStrategy {

    TOPIC_RESULT {
        @Override
        public <T extends Payload, E extends EventType> DomainEvent newInstance(T payload) {
            return DomainEvent.builder()
                    .payload(payload)
                    .type(this)
                    .build();
        }

        public String routingKey (){
            return "event.topic_result";
        }
    }
}
