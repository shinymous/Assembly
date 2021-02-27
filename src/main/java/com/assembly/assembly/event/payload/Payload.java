package com.assembly.assembly.event.payload;

import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public interface Payload {
    default String domainName() {
        return this.getClass().getSimpleName().toLowerCase();
    }
}
