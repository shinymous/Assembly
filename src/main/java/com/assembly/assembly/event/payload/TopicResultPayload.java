package com.assembly.assembly.event.payload;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TopicResultPayload implements Payload {
    private Long id;
    private String test;
}
