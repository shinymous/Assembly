package com.assembly.assembly.event.payload;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TopicResultPayload implements Payload {

    private Long id;
    private String name;
    private String description;
    private Long numberNoVotes;
    private Long numberYesVotes;
    private Result result;

    public enum Result{
        SIM,
        NAO,
        EMPATE
    }
}
