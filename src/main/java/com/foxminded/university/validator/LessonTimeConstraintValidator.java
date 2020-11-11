package com.foxminded.university.validator;

import com.foxminded.university.domain.LessonTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

public class LessonTimeConstraintValidator implements ConstraintValidator<ValidLessonTime, LessonTime> {

    @Override
    public void initialize(ValidLessonTime constraintAnnotation) {

    }

    @Override
    public boolean isValid(LessonTime lessonTime, ConstraintValidatorContext constraintValidatorContext) {
        if ((lessonTime.getBegin() != null) && (lessonTime.getEnd() != null)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Begin can not be after end")
                    .addPropertyNode("begin")
                    .addConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("End can not be before begin")
                    .addPropertyNode("end")
                    .addConstraintViolation();
            return Duration.between(lessonTime.getBegin(), lessonTime.getEnd()).toMinutes() > 0;
        }
        return true;
    }
}
