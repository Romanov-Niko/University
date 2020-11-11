package com.foxminded.university.validator;

import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.domain.Person;
import com.foxminded.university.domain.Teacher;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberConstraintValidator implements ConstraintValidator<ValidPhoneNumber, Person> {

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {
        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        if (person.getPhoneNumber() != null && !person.getPhoneNumber().equals("")) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Phone number must be valid")
                    .addPropertyNode("phoneNumber")
                    .addConstraintViolation();
            return person.getPhoneNumber().matches(patterns);
        } else {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Teacher must have phone number")
                    .addPropertyNode("phoneNumber")
                    .addConstraintViolation();
            return !(person instanceof Teacher);
        }
    }
}
