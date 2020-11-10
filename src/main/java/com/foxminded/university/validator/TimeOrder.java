package com.foxminded.university.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {TimeOrderConstraintValidator.class})
public @interface TimeOrder {

    String message() default "The time must be in the correct order";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
