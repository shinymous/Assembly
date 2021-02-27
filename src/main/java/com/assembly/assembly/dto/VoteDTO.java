package com.assembly.assembly.dto;

import com.assembly.assembly.util.CpfUtil;
import com.assembly.assembly.validator.CpfValidatorConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VoteDTO {

    @CpfValidatorConstraint
    @NotNull
    private String cpf;
    @NotNull
    private VoteEnum vote;

    public enum VoteEnum {
        SIM,
        NAO;
        public Short getValue(){
            if(this.name().equals("SIM"))
                return 1;
            return 2;
        }
    }

}
