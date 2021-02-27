package com.assembly.assembly.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CpfValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CpfValidatorConstraint {
    String message() default "Invalid cpf";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
