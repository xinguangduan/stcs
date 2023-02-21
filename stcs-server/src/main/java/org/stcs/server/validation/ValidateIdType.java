package org.stcs.server.validation;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IdTypeValidator.class)
public @interface ValidateIdType {

    String message() default "Invalid id: It should be  greater than  0 ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}