package com.assembly.assembly.event.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public interface EventType {
    String name();

    @JsonIgnore
    String routingKey();
}
