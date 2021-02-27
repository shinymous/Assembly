package com.assembly.assembly.event.config;

import com.assembly.assembly.event.payload.Payload;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class EventProcessor {

    private final EventConfig eventConfig;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Listener que publica mensagem
     * @param event
     * @param <T>
     * @param <E>
     */
    @EventListener(DomainEvent.class)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT,fallbackExecution = true)
    public <T extends Payload,E extends EventType> void onEvent(DomainEvent<T,E> event) {
        event.setApplication(eventConfig.getApplication());
        event.setCreated(LocalDateTime.now());
        event.setUuid(UUID.randomUUID());
        rabbitTemplate.convertAndSend(
                eventConfig.getEventTopicName(),
                event.getType().routingKey(),
                event
        );
    }
}