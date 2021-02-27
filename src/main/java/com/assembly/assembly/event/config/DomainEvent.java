package com.assembly.assembly.event.config;

import com.assembly.assembly.event.payload.Payload;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DomainEvent<T extends Payload,E extends EventType> {

    private UUID uuid;
    private LocalDateTime created;

    private String application;

    private T payload;
    private E type;

}