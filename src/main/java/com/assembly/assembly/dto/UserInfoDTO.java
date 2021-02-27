package com.assembly.assembly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserInfoDTO {

    private StatusEnum status;

    public enum StatusEnum {
        UNABLE_TO_VOTE,
        ABLE_TO_VOTE
    }
}
