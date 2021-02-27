package com.assembly.assembly.validator;

import com.assembly.assembly.util.CpfUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CpfValidator implements
        ConstraintValidator<CpfValidatorConstraint, String> {

    @Override
    public void initialize(CpfValidatorConstraint cpf) {
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext constraintValidatorContext) {
        return CpfUtil.isValidCpf(CpfUtil.removeSpecialCharacters(cpf));
    }

}
