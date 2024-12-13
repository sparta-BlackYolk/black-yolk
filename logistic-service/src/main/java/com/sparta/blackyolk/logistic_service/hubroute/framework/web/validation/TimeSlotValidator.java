package com.sparta.blackyolk.logistic_service.hubroute.framework.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class TimeSlotValidator implements ConstraintValidator<ValidTimeSlot, String> {

    // 허용된 시간 목록
    private static final Set<String> ALLOWED_TIME_SLOTS = Set.of(
          "22:00-05:00",
          "05:00-07:00",
          "07:00-09:00",
          "09:00-17:00",
          "17:00-20:00",
          "20:00-22:00"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null을 허용
        }
        return ALLOWED_TIME_SLOTS.contains(value);
    }
}
