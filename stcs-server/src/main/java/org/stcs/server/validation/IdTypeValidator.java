package org.stcs.server.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdTypeValidator implements ConstraintValidator<ValidateIdType, Integer> {
    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
//        List<String> employeeTypes = Arrays.asList("Permanent", "vendor");
        return (id > 0);
    }
}