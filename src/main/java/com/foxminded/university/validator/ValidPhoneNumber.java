package com.foxminded.university.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PhoneNumberConstraintValidator.class})
public @interface ValidPhoneNumber {

    String message() default "Phone number must be valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
