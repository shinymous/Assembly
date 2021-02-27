package com.assembly.assembly.dto;

import com.assembly.assembly.conversors.CustomLocalDateTimeDeserializer;
import com.assembly.assembly.conversors.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TopicDTO {

    @ApiModelProperty(hidden = true)
    private Long id;
    @NotNull(message = "Name can not be null. Please insert a value")
    private String name;
    @NotNull(message = "endLocalDateTime can not be null. Please insert a value")
    @ApiModelProperty(value = "yyyy-MM-dd HH:mm", example = "2000-01-01 00:00")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime endLocalDateTime;
    private String description;
}
