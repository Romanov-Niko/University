package com.foxminded.university.validator;

import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.domain.Student;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

public class DateOrderConstraintValidator implements ConstraintValidator<DateOrder, Student> {

    @Override
    public void initialize(DateOrder constraintAnnotation) {

    }

    @Override
    public boolean isValid(Student student, ConstraintValidatorContext constraintValidatorContext) {
        if ((student.getAdmission() != null) && (student.getGraduation() != null)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Admission can not be after graduation")
                    .addPropertyNode("admission")
                    .addConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Graduation can not be before admission")
                    .addPropertyNode("graduation")
                    .addConstraintViolation();
            return Duration.between(student.getAdmission().atStartOfDay(), student.getGraduation().atStartOfDay()).toDays() > 0;
        }
        return true;
    }
}
